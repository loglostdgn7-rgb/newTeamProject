// ================================================================
// review.js 파일 전체 내용
// ================================================================

document.addEventListener('DOMContentLoaded', () => {
    // 페이지가 로드되면 포토리뷰와 일반리뷰를 모두 불러옵니다.
    loadPhotoReviews();
    loadNormalReviews();
});

// ----------------------------------------------------------------
// [새로 추가] 포토리뷰 로딩 함수
// ----------------------------------------------------------------
function loadPhotoReviews() {
    const photoReviewContainer = document.getElementById('photo-review-container');
    if (!photoReviewContainer) return;

    // ※ 중요: 이 API는 백엔드에 새로 만들어야 합니다! (예: 4개만 가져오기)
    fetch('/review/list/photo?size=4')
        .then(response => response.json())
        .then(data => {
            photoReviewContainer.innerHTML = ''; // 컨테이너 비우기

            data.reviews.forEach(review => {
                if (review.base64ImageData) { // 이미지가 있는 리뷰만 처리
                    const photoItem = document.createElement('div');
                    photoItem.className = 'photo-review-item'; // HTML 구조에 맞게 클래스 설정

                    // 포토리뷰 카드 HTML 구조 (실제 디자인에 맞게 수정 필요)
                    photoItem.innerHTML = `
                        <div class="photo-review-all-item">
                            <div class="prhoto-review-item">
                                <div class="photo-review-image">
                                    <img src="${review.base64ImageData}" alt="포토 리뷰 이미지">
                                </div>
                                <div class="photo-review-product">
                                    <div class="product-image">
                                        <img src="${review.product && review.product.base64ImageData ? review.product.base64ImageData : ''}" alt="상품 썸네일">
                                    </div>
                                    <div class="product-detail">
                                        <div class="product-name">
                                            <span>${review.product ? review.product.name : ''}</span>
                                        </div>
                                    </div>
                                </div>
                                <div class="photo-review-text">
                                    <span>${review.content}</span>
                                </div>
                                <div class="photo-review-star">
                                    <span>★★★★☆</span> </div>
                                <div class="photo-review-created_at">
                                    <span>${review.at}</span>
                                </div>
                                <div class="user-name">
                                    <span>${review.user ? review.user.nickname : '익명'}</span>
                                </div>
                            </div> 
                        </div>
                    `;
                    photoReviewContainer.appendChild(photoItem);
                }
            });
        })
        .catch(err => console.error('포토리뷰 로드 오류:', err));
}


// ----------------------------------------------------------------
// [기존] 일반리뷰 로딩 함수 (이름을 loadNormalReviews로 변경)
// ----------------------------------------------------------------
let page = 1;
const size = 20; // 한 번에 너무 많이 부르면 느려지므로 20개 정도로 조절
const normalReviewContainer = document.getElementById("normal-review-container");
const loadMoreBtn = document.getElementById("load-more-btn");

function loadNormalReviews() {
    fetch(`/review/list?page=${page}&size=${size}`)
        .then(response => response.json())
        .then(data => {
            if (!data.reviews || data.reviews.length === 0) {
                if (page === 1) { // 첫 페이지부터 데이터가 없으면 컨테이너에 메시지 표시
                    normalReviewContainer.innerHTML = '<p>작성된 리뷰가 없습니다.</p>';
                }
                loadMoreBtn.style.display = "none";
                return;
            }

            data.reviews.forEach((review) => {
                const reviewItem = document.createElement("div");
                reviewItem.className = 'normal-review-item';

                let imgSrc = "/images/default_icon.png"; // 기본 이미지 경로
                if (review.product.base64ImageData) {
                    imgSrc = review.product.base64ImageData;
                } else if (review.product && review.product.base64ImageData) {
                    imgSrc = review.product.base64ImageData;
                }


                const productName = review.product ? review.product.name : '상품 정보 없음';
                const authorName = review.user ? review.user.nickname : '익명';

                reviewItem.innerHTML = `
                    <div class="normal-review-product">
                        <div class="normal-product-image">
                            <img src="${imgSrc}" alt="리뷰 이미지">
                        </div>
                        <div class="normal-product-detail">
                            <div class="product-name">${productName}</div>
                            <div class="review-text">${review.content}</div>
                            <div class="review-footer">
                                <div class="review-author">작성자: ${authorName}</div>
                                <div class="review-date">${review.at}</div>
                            </div>
                        </div>
                    </div>
                `;
                normalReviewContainer.appendChild(reviewItem);
            });

            if (data.elements.length < size) {
                loadMoreBtn.style.display = "none";
            } else {
                page++;
            }
        })
        .catch(err => console.error('일반리뷰 로드 오류:', err));
}

if (loadMoreBtn) {
    loadMoreBtn.addEventListener("click", loadNormalReviews);
}