let page = 1;
const size = 5;
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
                reviewItem.className= 'normal-review-item';

                reviewItem.innerHTML = `
                    <div class="normal-review-product">
                        <div class="normal-product-image">
                            <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTh-iDPkVLT8KcVDyYnt8Vy7XLzvjxB0GGdbA&s" alt="상품 이미지">
                        </div>
                        <div class="normal-product-detail">
                            <div class="review-text">${review.content}</div>
                            <div class="review-footer">
                                <div class="review-author">작성자: ${review.user}</div>
                                <div class="review-date">${review.at}</div>
                            </div>
                        </div>
                    </div>
                `;
                reviewContainer.appendChild(reviewItem);
            });

            if(page < data.totalPageCount){
                page++;
            }
            else{
                loadMoreBtn.style.display = "none";
            }
        })
}

loadMoreBtn.addEventListener("click", loadMore);

loadMore();



//
// function loadReviews() {
//     fetch(`/review/list?page=${page}&size=${size}`)
//         .then(res => res.json())
//         .then(data => {
//             if (!data.reviews || data.reviews.length === 0) {
//                 loadMoreBtn.style.display = "none";
//                 return;
//             }
//
//             data.reviews.forEach(r => {
//                 const reviewItem = document.createElement("div");
//                 reviewItem.className = "normal-review-item";
//
//                 reviewItem.innerHTML = `
//                     <div class="normal-review-product">
//                         <div class="normal-product-image">
//                             <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTh-iDPkVLT8KcVDyYnt8Vy7XLzvjxB0GGdbA&s" alt="상품 이미지">
//                         </div>
//                         <div class="normal-product-detail">
//                             <div class="review-text">${r.content}</div>
//                             <div class="review-footer">
//                                 <div class="review-author">작성자: ${r.author}</div>
//                                 <div class="review-date">${r.at}</div>
//                             </div>
//                         </div>
//                     </div>
//                 `;
//                 reviewContainer.appendChild(reviewItem);
//             });
//
//             if (page < data.totalPageCount) {
//                 page++;
//             } else {
//                 loadMoreBtn.style.display = "none";
//             }
//         })
//         .catch(err => console.error(err));
// }
//
// // 버튼 클릭 이벤트
// loadMoreBtn.addEventListener("click", loadReviews);
//
// // 첫 페이지 로드
// loadReviews();