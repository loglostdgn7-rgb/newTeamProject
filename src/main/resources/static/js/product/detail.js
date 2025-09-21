document.addEventListener('DOMContentLoaded', () => {
    const productPriceElement = document.getElementById("productPrice"); // 제품 가격
    const quantityInput = document.getElementById("quantity");           // 수량 input
    const totalPriceSpan = document.getElementById("totalPrice");        // 총 금액 표시
    const plusBtn = document.getElementById("quantityPlus");
    const minusBtn = document.getElementById("quantityMinus");

    // 가격 숫자로 변환
    const unitPrice = parseInt(productPriceElement.textContent.replace(/[^0-9]/g, ''), 10) || 0;

    function updateTotalPrice() {
        const quantity = parseInt(quantityInput.value, 10) || 1;
        const total = unitPrice * quantity;
        totalPriceSpan.textContent = `총 금액: ${total.toLocaleString()}원`;
    }

    // 초기 표시
    updateTotalPrice();

    // 수량 직접 입력 시
    quantityInput.addEventListener('input', updateTotalPrice);

    // + 버튼
    plusBtn.addEventListener('click', () => {
        quantityInput.value = parseInt(quantityInput.value, 10) + 1;
        updateTotalPrice();
    });

    // - 버튼 (최소 1개)
    minusBtn.addEventListener('click', () => {
        const current = parseInt(quantityInput.value, 10);
        if (current > 1) {
            quantityInput.value = current - 1;
            updateTotalPrice();
        }
    });

    /********** 아래 [구매하기/장바구니 기능]을 [김영수]님이 최신 수정(9/20) ****************/

    const buyBtn = document.getElementById("buyBtn");
    const basketBtn = document.getElementById("addToCartBtn");

    //장바구니 추가
    const addBasket = () => {
        const productId = parseInt(document.getElementById("productId").value);
        const product_quantity = parseInt(quantityInput.value);
        const token = document.querySelector("meta[name='_csrf']").content;
        const header = document.querySelector("meta[name='_csrf_header']").content;

        // 검증
        if (isNaN(productId) || isNaN(product_quantity) || product_quantity < 1) {
            alert("올바른 상품 수량을 입력해주세요.");
            return; // 유효하지 않으면 함수를 여기서 중단시킵니다.
        }
        //보낼 데이터
        const basketData = {
            product: {
                id: productId
            },
            quantity: product_quantity,
            updateType: "add"
        }
        //통신. 넘겨주기
        fetch("/user/basket/update", {
            method: "post",
            headers: {
                "Content-Type": "application/json",
                [header]: token
            },
            body: JSON.stringify(basketData)
        })
            .then(response => {
                if (response.ok) return response.text();
                else {
                    throw Error("서버 응답 에러");
                }
            })
            .then(data => {
                console.log("장바구니 추가");
                if (!basketSection) {
                    animateCartIcon();
                }
            })
            .catch(error => {
                console.error("장바구니 추가중 에러 발생: " + error);
                alert("장바구니 추가 중에 에러가 발생했습니다. 다시 시도해 주세요");
            });
    };

    basketBtn.onclick = addBasket;
    buyBtn.onclick = () => {
        addBasket();
        if (confirm("장바구니로 이동하시겠습니까?")) {
            location.href = "/user/basket";
        }
    }


});
