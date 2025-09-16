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
        const amountSpan = li.querySelector(".product-amount");
        //실제 수량
        let currentQuantity = parseInt(amountSpan.textContent);
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
                    productId: productId,
                    quantity: newQuantity
                })
            })
                .then(response => {
                    if (response.ok) {
                        amountSpan.textContent = newQuantity;
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
                } else throw Error("서버 응답 실패");
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

const closeModal = () => {
    if (openModalBackground) {
        openModalBackground.style.display = "none";
        openModalBackground = null;
    }
};

//배경 클릭 시 모달창 닫기
window.addEventListener("click", event => {
    const isModal = event.target.closest("div.payment-container");

    if (event.target === paymentBackground && !isModal)
        if (confirm("결제가 중지 됩니다. 정말 닫으시겠습니까?")) closeModal();
})

// paymentBackground.addEventListener("click", () => {
//     if (!paymentContainer) closeModal();
//
// });




