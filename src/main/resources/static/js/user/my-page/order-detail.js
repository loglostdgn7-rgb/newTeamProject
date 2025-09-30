const cancelOrReturnButton = document.querySelectorAll(".cancel-or-refund-order");
const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');


// modal 리뷰 등록 버튼
// HTML 문서의 로딩이 완료되었을 때, 해당 함수를 실행
// elements
const modal = document.querySelector('.modal');
const openBtns = document.querySelectorAll('.modalBtn');
const closeBtn = document.querySelector('.close-btn');

openBtns.forEach(btn => {
    btn.addEventListener('click', (e) => {
        const productRow = e.target.closest('.order-detail-grid-contents');
        const imgSrc = productRow.querySelector('img').src;
        const name = productRow.querySelector('span').textContent;
        const price = productRow.querySelector('.order-detail-price span').textContent;
        const id = productRow.querySelector('p').textContent;

        modal.querySelector('.product-image img').src = imgSrc;
        modal.querySelector('.product-name span').textContent = name;
        modal.querySelector('.product-price span').textContent = price;
        modal.querySelector('#productIdInput').value = id;
        modal.style.display = 'block';
    });
});

closeBtn.addEventListener('click', () => {
    modal.style.display = 'none';
});


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
            .then(message => {
                alert(message);
                location.reload();
            })
            .catch(error => {
                console.error("취소/반품 도중 실패: " + error)
                alert("취소/반품 도중 에러가 발생 했습니다. 다시 시도해 주세요");
            })
    }
});

//상품 개별 반품 버튼
const individualRefundButtons = document.querySelectorAll(".individual-refund-btn");

individualRefundButtons.forEach(button => {
    button.onclick = event => {
        const orderDetailId = event.target.dataset.orderDetailId;

        if (!confirm("해당 상품을 반품 하시겠습니까?")) return;

        fetch(`/user/my-page/order/detail/${orderDetailId}/refund`, {
            method: "post",
            headers: {
                "Content-Type": "application/json",
                [header]: token
            }
        })
            .then(response => {
                if (response.ok) return response.text();
                else throw Error("서버 응답 에러");
            })
            .then(message => {
                alert(message);
                location.reload();
            })
            .catch(error => {
                console.error("개별 상품 반품 처리 실패", error);
                alert("개별 상품 반품 처리 도중 에러 발생. 다시 시도해 주세요");
            });
    }
});


// 업로드 이미지 처리하기
const thumbnailImage = document.querySelector('.image > img');
let labelFileInput = document.querySelector('input[type=file]');

labelFileInput.onchange = add_content;

function add_content(event) {
    const files = event.target.files;
    if (files.length !== 1) {
        return;
    }
    const file = files[0];
    const blobURL = URL.createObjectURL(file);
    thumbnailImage.src = blobURL;
    thumbnailImage.style.display = 'block';

    const fileInput = document.getElementById('upload');
    const preview = document.querySelector('.image-box');
    const labelText = document.querySelector('.image > span');

    fileInput.addEventListener('change', () => {
        const file = fileInput.files[0];
        var imageSrc;
        imageSrc = URL.createObjectURL(file);

        preview.src = imageSrc;
        preview.style.display = 'block';   // 미리보기 표시
        labelText.style.display = 'none';  // 안내 텍스트 숨김
    });
}












// 업로드 이미지 처리하기
const thumbnailImage = document.querySelector('.image > img');
let labelFileInput = document.querySelector('input[type=file]');

labelFileInput.onchange = add_content;

function add_content(event) {
    const files = event.target.files;
    if (files.length !== 1) {
        return;
    }
    const file = files[0];
    const blobURL = URL.createObjectURL(file);
    thumbnailImage.src = blobURL;
    thumbnailImage.style.display = 'block';

    const fileInput = document.getElementById('upload');
    const preview = document.querySelector('.image-box');
    const labelText = document.querySelector('.image > span');

    fileInput.addEventListener('change', () => {
        const file = fileInput.files[0];
        var imageSrc;
        imageSrc = URL.createObjectURL(file);

        preview.src = imageSrc;
        preview.style.display = 'block';   // 미리보기 표시
        labelText.style.display = 'none';  // 안내 텍스트 숨김
    });
}














