//상품 종류 갯수 표시
const basketProductSortSpan = document.querySelector(".product-sort");

const basket_product_sort_count = ()=>{
    const lis = document.querySelectorAll(".product");
    basketProductSortSpan.textContent = `(${lis.length})`;
}

// 토큰
const token = document.querySelector("meta[name='_csrf']").content;
const header = document.querySelector("meta[name='_csrf_header']").content;

//장바구니 서머리
const update_basket_summary = () => {
    fetch("/user/basket/summary")
        .then(response => {
            if (response.ok) return response.json();
            else throw Error("가격 정보 로딩 실패");
        }).then(prices => {
        //가격을 한국 포맷으로 바꾸고
        const format = num => num.toLocaleString("ko-KR");

        document.querySelector(".product-total-price").textContent = format(prices.productTotalPrice);
        document.querySelector(".shipping-price").textContent = format(prices.shippingPrice);
        document.querySelector(".order-total").textContent = format(prices.orderTotalPrice);
    }).catch(error => console.error("가격 업데이트 중 에러: " + error));
}

//장바구니 체크
const noProductLi = document.querySelector(".no-product");

function check_basket_isEmpty() {
    //확인해줄 때마다 li들 확인
    const productLis = document.querySelectorAll(".product");
    if (productLis.length === 0) {
        noProductLi.style.display = "flex";
        document.querySelector(".shipping-price").textContent = "0";
    } else if (productLis.length > 0) noProductLi.style.display = "none";
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
            //자료 준비하고
            const updateData = {
                productId: productId,
                quantity: newQuantity
            }

            //통신하기
            fetch(`/user/basket/update`, {
                method: "post",
                headers: {
                    "Content-Type": "application/json",
                    [header]: token
                },
                body: JSON.stringify(updateData)
            }).then(response => {
                if (response.ok) {
                    amountSpan.textContent = newQuantity;
                    update_basket_summary();
                } else throw Error("서버 응답 실패");
            }).catch(error => console.error("수량 업데이트 중 실패") + error);
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
        const deleteUrl = aTag.href;
        //그리고 삭제
        fetch(deleteUrl, {
            method: "DELETE",
            headers: {
                [header]: token
            }
        }).then(response => {
            if (response.ok) {
                closestProductLi.remove();
                update_basket_summary();
                check_basket_isEmpty();
                basket_product_sort_count();
            } else throw Error("서버 응답 실패");
        }).catch(error => console.error("삭제 중 에러" + error));
    }
});
//장바구니 페이지에 처음 들어오면 확인
check_basket_isEmpty();
basket_product_sort_count();

/********************************************/

// const checkoutBtn = document.querySelector(".basket-total-container button");
//
// checkoutBtn.onclick = () => {
//     //체크아웃을 누르면 주문(order), 결제하는 곳으로 가도록 하기
//
//
// }