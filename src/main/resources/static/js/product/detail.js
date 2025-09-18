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


    /********** 아래 [구매하기/장바구니 기능]을 [김영수]님이 수정(9/19) ****************/
    const CartBtn = document.getElementById("addToCartBtn");
    const buyBtn = document.getElementById("buyBtn");
    // function getCookie(name) {
    //     const value = `; ${document.cookie}`;
    //     const parts = value.split(`; ${name}=`);
    //     if (parts.length === 2) return parts.pop().split(';').shift();
    //     return null;
    // }
    //
    // function addToCart(title, quantity) {
    //     let cart = getCookie('cart');
    //     if (cart) cart = JSON.parse(cart);
    //     else cart = [];
    //
    //     let exists = false;
    //     cart.forEach((item) => {
    //         if (item.title === title) {
    //             item.quantity += parseInt(quantity);
    //             exists = true;
    //         }
    //     });
    //
    //     if (!exists) {
    //         cart.push({ title, quantity: parseInt(quantity) });
    //     }
    //
    //     document.cookie = `cart=${JSON.stringify(cart)}; path=/; max-age=${60*60*24}`;
    // }

    //장바구니 추가
    const addBasket = () => {
        const productId = parseInt(document.getElementById("productId").value);
        // const product_quantity = parseInt(document.getElementsByClassName("quantity")[0].querySelector("input").value);
        const product_quantity = parseInt(quantityInput.value);
        // const product_title = document.getElementsByClassName("product-title")[0].innerText;
        // addToCart(product_title, product_quantity);
        // console.log(document.cookie);

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
                    alert("서버 응답 중 에러가 났습니다. 다시 시도해주세요");
                    throw Error("서버 응답 에러");
                }
            })
            .then(data => {
                console.log("장바구니 추가: " + data);
                alert("장바구니에 추가되었습니다");
                if (confirm("장바구니로 이동하시겠습니까?")) {
                    location.href = "/user/basket";
                }
            })
            .catch(error => {
                console.error("장바구니 추가중 에러 발생: " + error);
                alert("장바구니 추가 중에 에러가 발생했습니다. 다시 시도해 주세요");
            });
    };

    buyBtn.onclick = addBasket;
    CartBtn.onclick = addBasket;





});
