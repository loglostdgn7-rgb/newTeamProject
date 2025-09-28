// 검색 버튼/창
const searchBtnContainer = document.querySelector('.search-i-container');
const searchBtn = document.querySelector('.search-btn');

const searchInputContainer = document.querySelector('.search-input-container');
const searchInput = document.querySelector('.search-input');

searchBtn.onclick = () => {
    searchBtnContainer.style.display = "none";
    searchInputContainer.style.display = "flex";
    searchInput.focus();
}

searchInput.onblur = () => {
    searchInputContainer.style.display = "none";
    searchBtnContainer.style.display = "flex";
}

//검색창 결과 찾기
searchInput.onkeydown = event => {
    const searchValue = searchInput.value.trim();
    if (event.key === "Enter" && searchValue) {
        event.preventDefault();
        location.href = `/shop/product/search?searchValue=${searchValue}`;
    }
}

// NAV 버튼
const navButtons = document.querySelectorAll(".product-list-section > li");
const currentPath = window.location.pathname;

navButtons.forEach(button => {
    const aTag = button.querySelector("a");
    if (aTag && currentPath.includes(aTag.getAttribute("href"))) {
        button.classList.add("selected")
    }
});

// 헤더 카트 알림
const basketSection = document.querySelector(".basket-section");
const cartAlert = document.querySelector(".cart-container .cart-alert");

function animateCartIcon() {
    sessionStorage.setItem('cartVisible', 'true');

    cartAlert.style.display = 'flex';
    cartAlert.classList.add('pulse');

    setTimeout(() => {
        cartAlert.classList.remove('pulse');
    }, 500);
}

//장바구니 페이지면 안 보이게
if (cartAlert) {
    if (basketSection) {
        cartAlert.style.display = 'none';
        sessionStorage.removeItem('cartVisible');
    } else {
        if (sessionStorage.getItem('cartVisible') === 'true') {
            cartAlert.style.display = 'flex';
        } else {
            cartAlert.style.display = 'none';
        }
    }
}


//관리자 초기화 버튼
const adminResetBtn = document.querySelector('.admin-reset-button');

if (adminResetBtn) {
    adminResetBtn.addEventListener('click', () => {
        if (!confirm("[관리자] 모든 테스트 유저의 [프로필]과, [주문 정보 상태]를 초기화 하시겠습니까?")) {
            return;
        }

        const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
        const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

        fetch('/admin/reset-all', {
            method: 'post',
            headers: {
                [header]: token
            }
        })
            .then(response => {
                if (response.ok) {
                    return response.text();
                }
                throw new Error('서버 응답 오류');
            })
            .then(message => {
                alert(message);
                location.reload();
            })
            .catch(error => {
                console.error('관리자 초기화 중 오류:', error);
                alert('초기화 중 오류가 발생했습니다.');
            });
    });
}


// 로그아웃 Form
const logoutForm = document.querySelector(".logout-form");

if (logoutForm) {
    logoutForm.onsubmit = event => {
        if (!confirm("정말 로그아웃 하시겠습니까?")) //취소하면 preventDefault();
            event.preventDefault();
    }
}
