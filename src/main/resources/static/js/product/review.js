let page = 1;
const size = 100;
const reviewContainer = document.getElementById("normal-review-container");
const loadMoreBtn = document.getElementById("load-more-btn");

function loadMore() {
    fetch(`/review/list?page=${page}&size=${size}`)
        .then(response => response.json())
        .then(data => {
            if (!data.reviews || data.reviews.length === 0) {
                loadMoreBtn.style.display = "none";
                return;
            }
            data.reviews.forEach((review) => {
                const reviewItem = document.createElement("div");
                reviewItem.className = 'normal-review-item';

                // 서버에서 내려오는 base64 이미지가 있으면 넣고, 없으면 기본 이미지 사용
                const imgSrc = review.base64ImageData ? review.base64ImageData
                    : "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTh-iDPkVLT8KcVDyYnt8Vy7XLzvjxB0GGdbA&s";

                reviewItem.innerHTML = `
                    <div class="normal-review-product">
                        <div class="normal-product-image">
                            <img src="${imgSrc}" alt="상품 이미지">
                        </div>
                        <div class="normal-product-detail">
                            <div class="review-text">${review.content}</div>
                            <div class="review-footer">
                                <div class="review-author">작성자: ${review.user ? review.user.id : '익명'}</div>
                                <div class="review-date">${review.at}</div>
                            </div>
                        </div>
                    </div>
                `;
                reviewContainer.appendChild(reviewItem);
            });

            if (page < data.totalPageCount) {
                page++;
            } else {
                loadMoreBtn.style.display = "none";
            }
        })
        .catch(err => console.error('리뷰 로드 오류:', err));
}

loadMoreBtn.addEventListener("click", loadMore);

// 첫 페이지 로드
loadMore();
