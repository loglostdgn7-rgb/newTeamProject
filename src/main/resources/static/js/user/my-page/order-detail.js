const cancelOrReturnButton = document.querySelectorAll(".cancel-or-refund-order");
const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

cancelOrReturnButton.forEach(button => {
    button.onclick = event => {
        const orderId = event.target.dataset.orderId;
        const newStatus = event.target.dataset.status;
        let confirmMessage = newStatus === "CANCEL" ? "전체 상품 주문을 취소 하시겠습니까?" : "전체 상품을 반품 하시겠습니까?";

        if (!confirm(confirmMessage)) {
            return;
        }

        fetch(`/user/my-page/order/${orderId}/status`, {
            method: "post",
            headers: {
                "Content-Type": "application/json",
                [header]: token
            },
            body: JSON.stringify({status: newStatus})
        })
            .then(response => {
                if (response.ok) {
                    return response.text();

                } else {
                    throw Error("서버 응답 에러");
                }
            })
            .then(message=>{
                alert(message);
                location.reload();
            })
            .catch(error => {
                console.error("취소/반품 도중 실패: " + error)
                alert("취소/반품 도중 에러가 발생 했습니다. 다시 시도해 주세요");
            })
    }
});




