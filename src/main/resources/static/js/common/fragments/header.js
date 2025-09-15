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

// NAV 버튼
const navButtons = document.querySelectorAll(".product-list-section > li");
const currentPath = window.location.pathname;
let selectedButton = null;

navButtons.forEach(button => {
    const aTag = button.querySelector("a");
    if (aTag && currentPath.includes(aTag.getAttribute("href"))) {
        button.classList.add("selected")
    }
});

// 로그아웃 Form
const logoutForm = document.querySelector(".logout-form");

if (logoutForm) {
    logoutForm.onsubmit = event => {
        if(!confirm("정말 로그아웃 하시겠습니까?"))
        event.preventDefault();
    }
}