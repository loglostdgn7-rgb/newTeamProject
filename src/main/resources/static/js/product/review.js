// ================================================================
// review.js 파일 전체 내용 (최종 수정본)
// ================================================================

// 🌟 페이지 변수 분리 및 초기화
let photoPage = 1;
let normalPage = 1;
const size = 3;

// 🌟 전역 요소 정의 (getElementById 호출은 한 번만)
const photoReviewContainer = document.getElementById('photo-review-container');
const normalReviewContainer = document.getElementById("normal-review-container");
const photoloadMoreBtn = document.getElementById("photo-load-more-btn");
const loadMoreBtn = document.getElementById("load-more-btn");


document.addEventListener('DOMContentLoaded', () => {
    // 페이지가 로드되면 포토리뷰와 일반리뷰를 모두 불러옵니다.
    loadPhotoReviews();
    loadNormalReviews();

    // 🌟 이벤트 리스너 연결
    if (loadMoreBtn) {
        loadMoreBtn.addEventListener("click", loadNormalReviews);
    }

    if (photoloadMoreBtn) {
        photoloadMoreBtn.addEventListener("click", loadPhotoReviews);
    }
});


// ----------------------------------------------------------------
// [새로 추가] 포토리뷰 로딩 함수 (수정)
// ----------------------------------------------------------------
function loadPhotoReviews() {

    if (!photoReviewContainer) return;

    // 🌟 photoPage 사용
    fetch(`/review/list/photo?page=${photoPage}&size=${size}`)
        .then(response => response.json())
        .then(data => {

            // 🌟 1. 데이터가 없으면 버튼 숨김 (중복 로딩 방지를 위해 먼저 확인)
            if (!data.reviews || data.reviews.length === 0) {
                if (photoloadMoreBtn) photoloadMoreBtn.style.display = "none";
                return;
            }

            data.reviews.forEach(review => {
                if (review.base64ImageData) {
                    const photoItem = document.createElement('div');
                    photoItem.className = 'photo-review-item';

                    const productName = review.product ? review.product.name : '';
                    const productLink = review.product ? `/shop/product/detail/${review.product.productId}` : '#';
                    const productThumb = review.product ? review.product.base64ImageData : '';
                    const userNickname = review.user ? review.user.nickname : '익명';

                    photoItem.innerHTML = `
                        <div class="photo-review-image">
                            <img src="${review.base64ImageData}" alt="포토 리뷰 이미지">
                        </div>
                        <div class="photo-review-product">
                            <div class="product-image">
                                <img src="${productThumb}" alt="상품 썸네일">
                            </div>
                            <div class="product-detail">
                                <div class="product-name">
                                    <a href="${productLink}">
                                        <span>${productName}</span>
                                    </a>
                                </div>
                                <div class="product-photo-review-count">
                                    리뷰<span>3</span>    
                                </div>
                            </div>
                        </div>
                        <div class="photo-review-text">
                            <span>${review.content}</span>
                        </div>
                        <div class="photo-review-star">
                            <span>★★★★☆</span>
                        </div>
                        <div class="photo-review-created_at">
                            <span>${review.at}</span>
                        </div>
                        <div class="user-name">
                            <span>${userNickname}</span>
                        </div>
                    `;

                    photoReviewContainer.appendChild(photoItem);
                }
            });

            // 🌟 중복 로딩 버그 해결: 다음 페이지로 이동 로직 추가
            if (data.reviews.length === size) {
                photoPage++; // 👈 이 부분이 누락되어 중복이 발생했습니다.
            } else {
                // 마지막 페이지일 경우 '더 보기' 버튼 숨김
                if (photoloadMoreBtn) photoloadMoreBtn.style.display = "none";
            }
        })
        .catch(err => console.error('포토리뷰 로드 오류:', err));
}


// ----------------------------------------------------------------
// [기존] 일반리뷰 로딩 함수 (수정)
// ----------------------------------------------------------------
function loadNormalReviews() {

    // 🌟 normalPage 사용
    fetch(`/review/list?page=${normalPage}&size=${size}`)
        .then(response => response.json())
        .then(data => {

            // 🌟 data.reviews 사용 (기존 코드 유지)
            if (!data.reviews || data.reviews.length === 0) {
                if (normalPage === 1) {
                    normalReviewContainer.innerHTML = '<p>작성된 리뷰가 없습니다.</p>';
                }
                if (loadMoreBtn) loadMoreBtn.style.display = "none";
                return;
            }

            data.reviews.forEach((review) => {
                if (review.base64ImageData == null) {
                    const reviewItem = document.createElement("div");
                    reviewItem.className = 'normal-review-item';
                    let imgSrc = "/images/default_icon.png";

                    // 🌟 이미지 로직 단순화
                    if (review.product && review.product.base64ImageData) {
                        imgSrc = review.product.base64ImageData;
                    }

                    const productName = review.product ? review.product.name : '상품 정보 없음';
                    const authorName = review.user ? review.user.nickname : '익명';
                    const productLink = review.product ? `/shop/product/detail/${review.product.productId}` : '#';

                    reviewItem.innerHTML = `
                        <div class="normal-review-product">
                            <div class="normal-product-image">
                                <img src="${imgSrc}" alt="리뷰 이미지">
                            </div>
                            <div class="normal-product-detail">
                                <div class="product-name">
                                    <a href="${productLink}">
                                        ${productName}
                                    </a>
                            </div>
                                <div class="review-text">${review.content}</div>
                                <div class="review-footer">
                                    <div class="review-author">작성자: ${authorName}</div>
                                    <div class="review-date">${review.at}</div>
                                </div>
                            </div>
                        </div>
                    `;
                    normalReviewContainer.appendChild(reviewItem);
                }
            });

            // 🌟 페이지 증가 로직 수정: normalPage 사용
            if (data.reviews.length < size) {
                if (loadMoreBtn) loadMoreBtn.style.display = "none";
            } else {
                normalPage++; // 🌟 다음 페이지 준비
            }
        })
        .catch(err => console.error('일반리뷰 로드 오류:', err));
}