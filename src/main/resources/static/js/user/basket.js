// 토큰
const token = document.querySelector("meta[name='_csrf']").content;
const header = document.querySelector("meta[name='_csrf_header']").content;

//상품 종류 갯수
const basketProductSortSpan = document.querySelector(".product-sort");

const basket_product_sort_count = () => {
    const lis = document.querySelectorAll(".product");
    basketProductSortSpan.textContent = `(${lis.length})`;
}

//장바구니 서머리
const update_basket_summary = () => {
    fetch("/user/basket/summary")
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                alert("가경 정보 로딩 실패. 다시 시도해 주세요");
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
const plus_minus_Btns = document.querySelectorAll(".bi");

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
                        alert("장바구니 담기에 실패하였습니다. 다시 시도해 주세요");
                        throw Error("서버 응답 실패");
                    }
                })
                .catch(error => {
                    console.error("수량 업데이트 중 실패: " + error);
                    alert("수량 업데이트에 에러가 발생했습니다. 다시 시도해 주세요");
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
                    alert("서버 응담실패. 다시 시도해 주세요");
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

//체크아웃을 누르면 주문(order), 결제창 뜨기
checkoutBtn.onclick = () => {
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

    if (event.target === paymentBackground && !isModal)
        if (confirm("결제가 중지 됩니다. 정말 닫으시겠습니까?")) closeModal();
});

//결제창 버튼
paymentBtn.onclick = () => {
    // 결제 검증
    //imp 초기화 하고
    IMP.init("imp76108135");

    //PG 사를 위해 적는 것들..
    const merchantUid = "ORD_" + crypto.randomUUID();
    console.log("merchantUid: " + merchantUid);
    const paymentForm = document.querySelector(".payment-container > form");
    const productList = document.querySelector(".product");
    const firstProductName = productList[0].querySelector(".product-name").textContent;
    const orderName = productList > 1 ? `${firstProductName} 외 ${productList - 1}건` : firstProductName;
    const buyerName = paymentForm.querySelector(".real-name").value;
    const buyerPostcode = paymentForm.querySelector(".postcode").value;
    const roadAddress = paymentForm.querySelector(".road-address").value;
    const detailAddress = paymentForm.querySelector(".detail-address").value;
    const buyerAddress = roadAddress.value + detailAddress.value;
    const telPrev = paymentForm.querySelector(".payment-tel-prev").value;
    const telBody = paymentForm.querySelector(".payment-tel-body").value;
    const telTail = paymentForm.querySelector(".payment-tel-tail").value;
    const buyerTel = telPrev + telBody + telTail;
    const buyerEmail = paymentForm.querySelector(".email").value;
    // const buyerRequest = paymentForm.querySelector(".request").value;
    const price = parseInt(paymentForm.querySelector(".price").textContent);
    const paymentMethod = paymentForm.querySelector("input[type=radio][name=payment]:checked").value;

    IMP.request_pay(
        {
            channelKey: "channel-key-99dbb0dc-a2a3-4159-ad15-f7095f2fd7d4",
            pay_method: paymentMethod,
            merchant_uid: merchantUid,
            name: orderName, //상품리스트 이름("첫번째상품이름...")
            amount: price,
            buyer_email: buyerEmail,
            buyer_name: buyerName,
            buyer_tel: buyerTel,
            buyer_addr: buyerAddress,
            buyer_postcode: buyerPostcode,
        },
        async (response) => {
            if (response.error_code != null) return alert(`결제에 실패했습니다. 에러: ${response.error_msg}`);

            const SERVER_BASE_URL = "http://localhost:8080"
            const notified = await fetch(`${SERVER_BASE_URL}/payment/complete`, {
                method: "post",
                headers: {"Content-Type": "application/json"},
                // imp_uid와 merchant_uid, 주문 정보를 서버에 전달합니다
                body: JSON.stringify({
                    imp_uid: response.imp_uid,
                    merchant_uid: response.merchant_uid,
                    pay_method: response.pay_method,
                    name: response.name, //상품리스트 이름("첫번째상품이름...")
                    amount: response.amount,
                    buyer_email: response.buyer_email,
                    buyer_name: response.buyer_name,
                    buyer_tel: response.buyer_tel,
                    buyer_addr: response.buyer_addr,
                    buyer_postcode: response.buyer_postcode,
                }),
            });
        },
    ); //request_pay 끝
}







