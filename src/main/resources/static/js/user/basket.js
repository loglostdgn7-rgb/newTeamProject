// 토큰
const token = document.querySelector("meta[name='_csrf']").content;
const header = document.querySelector("meta[name='_csrf_header']").content;

//상품 종류 갯수
const basketProductSortSpan = document.querySelector(".product-sort");

const basket_product_sort_count = () => {
    const lis = document.querySelectorAll(".basket-section .product");
    basketProductSortSpan.textContent = `(${lis.length})`;
}

//장바구니 서머리
const update_basket_summary = () => {
    fetch("/user/basket/summary")
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw Error("가격 정보 로딩 실패");
            }
        })
        .then(price => {
            document.querySelector(".product-total-price").textContent = price.productTotalPrice;
            document.querySelector(".shipping-price").textContent = price.shippingPrice;
            document.querySelector(".order-total").textContent = price.orderTotalPrice;
        })
        .catch(error => {
            console.error("가격 업데이트 중 에러: " + error);
            alert("가격 업데이트중 에러가 발생. 다시 시도해 주세요");
        });
}

//장바구니 체크
const noProductLi = document.querySelector(".no-product");

function check_basket_isEmpty() {
    //확인해줄 때마다 li들 확인
    const productLis = document.querySelectorAll(".product");
    if (productLis.length === 0) {
        noProductLi.style.display = "flex";
        document.querySelector(".product-total-price").textContent = "₩0";
        document.querySelector(".shipping-price").textContent = "₩0";
        document.querySelector(".order-total").textContent = "₩0";
    } else if (productLis.length > 0) {
        noProductLi.style.display = "none";
    }
}


// 플러스 마이너스 버튼 작용
const basketContainer = document.querySelector(".basket-container");
const plus_minus_Btns = basketContainer.querySelectorAll(".bi");

plus_minus_Btns.forEach(iTag => {
    iTag.onclick = event => {
        //부모먼저 찾고
        const li = event.currentTarget.closest("li.product");
        //그의 자식인 element를 찾는다.
        const productId = li.dataset.productId;
        const quantitySpan = li.querySelector(".product-quantity");
        //실제 수량
        let currentQuantity = parseInt(quantitySpan.textContent);
        let newQuantity = currentQuantity;

        if (event.currentTarget.classList.contains("bi-plus")) newQuantity++;
        else if (event.currentTarget.classList.contains("bi-dash") && currentQuantity > 1) newQuantity--;

        //수량이 변경 되었으면
        if (newQuantity !== currentQuantity) {
            //통신하기
            fetch(`/user/basket/update`, {
                method: "post",
                headers: {
                    "Content-Type": "application/json",
                    [header]: token
                },
                body: JSON.stringify({
                    product: {
                        id: productId,
                    },
                    quantity: newQuantity,
                    updateType: "overwrite"
                })
            })
                .then(response => {
                    if (response.ok) {
                        quantitySpan.textContent = newQuantity;
                        update_basket_summary();
                    } else {
                        throw Error("서버 응답 실패");
                    }
                })
                .catch(error => {
                    console.error("장바구니 담기/수량 업데이트 중 실패: " + error);
                    alert("장바구니 담기/수량 업데이트에 에러가 발생했습니다. 다시 시도해 주세요");
                });
        }
    }
});

//장바구니 상품 삭제
const removeBtns = document.querySelectorAll(".product-remove-box > a");

removeBtns.forEach(aTag => {
    aTag.onclick = event => {
        event.preventDefault();
        //event에 가장 가까운 부모 li 찾기
        const closestProductLi = event.currentTarget.closest("li.product");
        const deleteUrl = aTag.href; //이미 html에 url 입력 했었네
        //그리고 삭제
        fetch(deleteUrl, {
            method: "DELETE",
            headers: {
                [header]: token
            }
        })
            .then(response => {
                if (response.ok) {
                    closestProductLi.remove();
                    check_basket_isEmpty();
                    //시간 여유 주기
                    setTimeout(() => {
                        update_basket_summary();
                    }, 300)
                    basket_product_sort_count();
                } else {
                    throw Error("서버 응답 실패");
                }
            })
            .catch(error => console.error("삭제 중 에러" + error));
    }
});
//장바구니 페이지에 처음 들어오면 확인
check_basket_isEmpty();
basket_product_sort_count();

/********************************************/
// 결제 modal 창
const checkoutBtn = document.querySelector(".basket-total-container .check-out");
const paymentBackground = document.querySelector(".payment-background");
const paymentContainer = paymentBackground.querySelector(".payment-container");
let openModalBackground = null;
const paymentBtn = paymentContainer.querySelector(".payment-button");
const productList = document.querySelectorAll(".product"); //select All
const paymentForm = document.querySelector(".payment-container > form");

//체크아웃을 누르면 주문(order), 결제창 뜨기
checkoutBtn.onclick = () => {
    const refreshProductLi = document.querySelectorAll(".product");

    //상품 없으면 체크아웃 방지
    if (refreshProductLi.length === 0) return alert("장바구니에 상품이 없습니다");

    const refreshOrderTotal = document.querySelector(".order-total").textContent;
    paymentForm.querySelector(".order-total-price").value = refreshOrderTotal;

    paymentBackground.style.display = "block";
    openModalBackground = paymentBackground; //지금 나타난 것은 paymentBackground
}

//모달창 기능
const closeModal = () => {
    if (openModalBackground) {
        openModalBackground.style.display = "none";
        openModalBackground = null;
    }
};

//배경 클릭 시 모달창 [닫기]
window.addEventListener("click", event => {
    const isModal = event.target.closest("div.payment-container");
    //그냥 누르면 닫기. 어차피 결제창 따로 있다.
    if (event.target === paymentBackground && !isModal) closeModal();
});

//결제창 버튼
paymentBtn.onclick = () => {
    const refreshProductLi = document.querySelectorAll(".product");
    //imp 초기화 하고
    IMP.init("imp76108135");

    const merchantUid = "ORD_" + crypto.randomUUID();
    const firstProductName = refreshProductLi[0].querySelector(".product-name").textContent;
    const orderName = refreshProductLi.length > 1 ? `${firstProductName} 외 ${refreshProductLi.length - 1}건` : firstProductName;
    const buyerName = paymentForm.querySelector(".real-name").value;
    const buyerPostcode = paymentForm.querySelector(".postcode").value;
    const roadAddress = paymentForm.querySelector(".road-address").value;
    const detailAddress = paymentForm.querySelector(".detail-address").value;
    const buyerAddress = roadAddress + detailAddress;
    const telPrev = paymentForm.querySelector(".payment-tel-prev").value;
    const telBody = paymentForm.querySelector(".payment-tel-body").value;
    const telTail = paymentForm.querySelector(".payment-tel-tail").value;
    const buyerTel = telPrev + telBody + telTail;
    const buyerEmail = paymentForm.querySelector(".email").value;
    const orderRequest = paymentForm.querySelector(".request").value;
    const priceString = paymentForm.querySelector(".order-total-price").value;
    const amount = priceString.replace(/[^0-9]/g, ""); //정규식 써서 숫자 외 모두 제거
    console.log("price:", amount)
    //체크된 결제 방식이 있느냐?
    const isCheckedPaymentMethod = paymentForm.querySelector("input[type=radio][name=payment-method]:checked");

    //결제 방식을 선택 안해도 결제 방지
    if (isCheckedPaymentMethod == null) return alert("결제 방식을 선택하세요");

    const paymentMethod = isCheckedPaymentMethod.value;

    IMP.request_pay(
        {
            channelKey: "channel-key-95a34a9c-f92d-40ec-8f63-66d1bb78bcb0",
            pay_method: paymentMethod,
            merchant_uid: merchantUid,
            name: orderName, //상품리스트 이름("첫번째상품이름...")
            amount: amount,
            buyer_email: buyerEmail,
            buyer_name: buyerName,
            buyer_tel: buyerTel,
            buyer_addr: buyerAddress,
            buyer_postcode: buyerPostcode
        },
        async (response) => {
            if (response.success) {
                //각 상품마다 표시해주기 -> 그리고 아래 dataForBody에 넣기..
                const orderDetail = Array.from(refreshProductLi).map(productLi => {
                    const priceSpan = productLi.querySelector("span[data-product-price]");
                    const price = priceSpan.dataset.productPrice;
                    return {
                        product_id: productLi.dataset.productId,
                        product_price: price,
                        quantity: productLi.querySelector(".product-quantity").textContent,
                    }
                })

                const dataForBody = {
                    imp_uid: response.imp_uid,
                    merchant_uid: merchantUid,
                    pay_method: paymentMethod,
                    order_name: orderName,
                    order_price: amount, // 숫자만 추출한 amount 변수 사용
                    buyer_email: buyerEmail,
                    buyer_name: buyerName,
                    buyer_tel: buyerTel,
                    buyer_addr: buyerAddress,
                    buyer_postcode: buyerPostcode,
                    order_details: orderDetail, // orderDTO 필드명(orderDetails)에 맞춰 s 붙이기
                    order_request: orderRequest,
                };

                console.log("서버로 보내는 데이터:", dataForBody); //확인용

                await fetch(`/user/payment/complete`, {
                    method: "post",
                    headers: {
                        "Content-Type": "application/json",
                        [header]: token
                    },
                    // imp_uid와 merchant_uid, 주문 정보를 서버에 전달합니다
                    body: JSON.stringify(dataForBody),
                });

                //주문내역 데이터 삽입 후
                if (confirm("결제가 되었습니다.\n주문 내역을 보시겠습니까?")) location.href = "/user/my-page/order";
                else location.reload();
            } //저 위에 if (response.success)문 끝
        }
    ); //request_pay 끝
    //검증은 패스...
}







