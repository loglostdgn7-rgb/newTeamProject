//드래그 방지
document.querySelectorAll('.swiper-slide').forEach(li => {
    li.addEventListener('mousedown', event => event.preventDefault());
});

//메세지
const indexSection = document.querySelector(".index");

if (indexSection.dataset.message){
    alert(indexSection.dataset.message);
}

/**************  swper  ******************/
const swiper = new Swiper('.swiper', {
    loop: true,
    slidesPerView: 4,      //  오타 수정 (slidePerView -> slidesPerView) 및 개수 조정
    slidesPerGroup: 1,      //  한 번에 1개 슬라이드만 넘어가도록 설정
    spaceBetween: 20,
    speed: 400,                 // 슬라이드 애니메이션 속도를 0.4초로 약간 빠르게 조정
    waitForTransitions: false, //  전환 대기 시간을 없애 연타가 가능하도록 설정
    allowTouchMove: false,
    autoplay: {
        delay: 4000,
        disableOnInteraction: false,
    },
    pagenation: {
        el: '.swiper-pagenation',
        clickable: true, // 점을 클릭해서 슬라이드 이동 가능
    },

    // 좌우 버튼 설정
        //아래에서 직접설정 해 줄것이므로 주석 처리
    // navigation: {
    //     nextEl: '.swiper-button-next',
    //     prevEl: '.swiper-button-prev',
    // },
});

const prevButton = document.querySelector(".swiper-button-prev");
const nextButton = document.querySelector(".swiper-button-next");

prevButton.addEventListener("click", ()=>{
    swiper.slidePrev(100);
})

nextButton.addEventListener("click", ()=>{
    swiper.slideNext(100);
})
