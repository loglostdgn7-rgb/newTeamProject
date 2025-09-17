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
    const CartBtn = document.getElementById("addToCartBtn");

    function getCookie(name) {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) return parts.pop().split(';').shift();
        return null;
    }

    function addToCart(title, quantity) {
        let cart = getCookie('cart');
        if (cart) cart = JSON.parse(cart);
        else cart = [];

        let exists = false;
        cart.forEach((item) => {
            if (item.title === title) {
                item.quantity += parseInt(quantity);
                exists = true;
            }
        });

        if (!exists) {
            cart.push({ title, quantity: parseInt(quantity) });
        }

        document.cookie = `cart=${JSON.stringify(cart)}; path=/; max-age=${60*60*24}`;
    }

    CartBtn.onclick = () => {
        const product_title = document.getElementsByClassName("product-title")[0].innerText;
        const product_quantity = document.getElementsByClassName("quantity")[0].querySelector("input").value;

        addToCart(product_title, product_quantity);
        console.log(document.cookie);

        alert("장바구니에 추가되었습니다");
        if(confirm("장바구니로 이동하시겠습니까?")){
            location.href = "/user/basket";
        }
    };

});
