const track = document.getElementById('catTrack');
const prev  = document.querySelector('.cat-prev');
const next  = document.querySelector('.cat-next');



function step() {
    // 화면의 80%만큼 이동 (최소 140px 보장)
    return Math.max(140, Math.round(track.clientWidth * 0.8));
}

function updateButtons() {
    const max = track.scrollWidth - track.clientWidth - 1;
    prev.disabled = track.scrollLeft <= 0;
    next.disabled = track.scrollLeft >= max;
}

prev.addEventListener('click', () => track.scrollBy({ left: -step(), behavior: 'smooth' }));
next.addEventListener('click', () => track.scrollBy({ left:  step(), behavior: 'smooth' }));

track.addEventListener('scroll', () => requestAnimationFrame(updateButtons));
updateButtons();

// 키보드 네비게이션
track.addEventListener('keydown', (e) => {
    if (e.key === 'ArrowLeft')  { e.preventDefault(); track.scrollBy({ left: -step(), behavior: 'smooth' }); }
    if (e.key === 'ArrowRight') { e.preventDefault(); track.scrollBy({ left:  step(), behavior: 'smooth' }); }
});

// 활성 상태 토글 (필요 없으면 제거)
track.addEventListener('click', (e) => {
    const item = e.target.closest('.cat-item');
    if (!item) return;
    track.querySelectorAll('.cat-item.is-active').forEach(el => el.classList.remove('is-active'));
    item.classList.add('is-active');
    // TODO: 여기서 실제 필터/라우팅 실행
    // console.log('Selected:', item.querySelector('.cat-link').dataset.label);
});

// category-carousel.js 맨 아래 init 내부 또는 DOMContentLoaded 이후 실행
(() => {
    const root = document.querySelector('.cat-carousel');
    if (!root) return;

    const items = Array.from(root.querySelectorAll('.cat-item'));
    const links = items.map(li => li.querySelector('a.cat-link'));

    function setActiveByLocation() {
        const current = window.location.pathname; // 예: /category/shoes
        let exact = null, partial = null;

        for (const li of items) li.classList.remove('is-active');

        for (let i = 0; i < links.length; i++) {
            const href = links[i].getAttribute('href');
            if (!href) continue;
            try {
                // 절대경로화
                const u = new URL(href, window.location.origin);
                if (u.pathname === current) {
                    exact = i; break;
                }
                if (current.startsWith(u.pathname)) {
                    partial = partial ?? i;
                }
            } catch(_) {}
        }

        const targetIndex = (exact ?? partial ?? 0);
        items[targetIndex]?.classList.add('is-active');
    }

    setActiveByLocation();
})();



const cat_links = document.getElementsByClassName("cat-link");
const select_category = document.getElementsByClassName("select-category")[0]; // 래퍼 div
const selected_p = select_category.querySelector('p'); // ← 상단 표시용 p 요소 (없으면 추가하세요)

for (let i = 0; i < cat_links.length; i++) {
    cat_links[i].addEventListener("click", function (e) {
        e.preventDefault();

        // 클릭한 항목 내부의 라벨 텍스트 읽기
        const label = this.querySelector(".cat-label").textContent.trim();

        // 상단 표시 텍스트 갱신 (전체상품은 특별 처리)
        selected_p.textContent = label;

        // (선택) 활성 표시 토글
        document.querySelectorAll(".cat-item.is-active").forEach(el => el.classList.remove("is-active"));
        this.closest(".cat-item")?.classList.add("is-active");
    });
}
