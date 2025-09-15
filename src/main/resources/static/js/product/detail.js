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
});
