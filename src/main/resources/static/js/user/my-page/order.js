// 날짜를 String으로 바꾸기
const formDate = date => {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}`;
};


const dropdownGroups = document.querySelectorAll(".order-select-container > li:has(.option-box)");
const selectButtons = document.querySelectorAll("label.select-box[data-target]");
let openOptionBox = null;

// 옵션 창 닫기
const closeOptionBox = () => {
    if (openOptionBox) {
        openOptionBox.style.display = "none";
        openOptionBox = null;
    }
};

// 드롭다운 버튼 클릭 이벤트
selectButtons.forEach(button => {
    button.addEventListener("click", () => {
        const targetBox = document.querySelector(`.${button.dataset.target}`);
        if (openOptionBox === targetBox) {
            closeOptionBox();
        } else {
            closeOptionBox();
            targetBox.style.display = "flex";
            openOptionBox = targetBox;
        }
    });
});

// 외부 클릭 시 드롭다운 닫기
window.addEventListener("click", (event) => {
    const isButton = event.target.closest("label.select-box[data-target]");
    const isOptionBox = event.target.closest("ul.option-box");
    if (!isButton && !isOptionBox) {
        closeOptionBox();
    }
});

// 옵션 선택 시 값 변경 이벤트
dropdownGroups.forEach(dropdown => {
    const input = dropdown.querySelector(".select-box-input");
    const options = dropdown.querySelectorAll(".option");

    options.forEach(option => {
        option.addEventListener("click", (event) => {
            event.preventDefault();
            const selectedText = option.textContent.trim();
            input.value = selectedText;

            if (dropdown.matches(".period-group")) {
                const startDateInput = document.getElementById("date-picker-start");
                const endDateInput = document.getElementById("date-picker-end");
                const today = new Date();
                const startDate = new Date();

                switch (selectedText) {
                    case "일주일":
                        startDate.setDate(today.getDate() - 7);
                        break;
                    case "1개월":
                        startDate.setMonth(today.getMonth() - 1);
                        break;
                    case "3개월":
                        startDate.setMonth(today.getMonth() - 3);
                        break;
                    case "6개월":
                        startDate.setMonth(today.getMonth() - 6);
                        break;
                }
                startDateInput.value = formDate(startDate);
                endDateInput.value = formDate(today);
            }
            closeOptionBox();
        });
    });
});


// 달력 기능
const dateInputs = document.querySelectorAll(".date-picker-input");
const startDateInput = document.getElementById("date-picker-start");
const endDateInput = document.getElementById("date-picker-end");
const getHistoryBtn = document.getElementById("get-history");


// 초기 날짜 설정
if (!startDateInput.value) {
    const weekAgo = new Date();
    weekAgo.setDate(new Date().getDate() - 7);
    startDateInput.value = formDate(weekAgo);
}
if (!endDateInput.value) {
    endDateInput.value = formDate(new Date());
}

// 라이브러리 초기화
dateInputs.forEach(input => {
    new Datepicker(input, {format: "yyyy-mm-dd", language: "ko", autohide: true});
});

// 날짜 유효성 검사 이벤트
endDateInput.addEventListener("changeDate", event => {
    const selectedDate = event.detail.date;
    const today = new Date();
    const startDay = startDateInput.datepicker.getDate();
    today.setHours(23, 59, 59, 999);

    if (selectedDate > today) {
        alert("오늘 이후로는 선택 할 수 없습니다!");
        endDateInput.datepicker.setDate(new Date());
    } else if (selectedDate < startDay) {
        alert("시작일보다 이전은 선택 할 수 없습니다!");
        endDateInput.datepicker.setDate(startDay);
    }
});

// 조회 버튼 클릭 이벤트
getHistoryBtn.onclick = () => {
    if (startDateInput.value && endDateInput.value) {
        location.href = `history?startDate=${startDateInput.value}&endDate=${endDateInput.value}`;
    } else {
        alert("조회 기간을 선택해주세요!");
    }
};


// 드래그 방지 기능
document.querySelectorAll('.no-drag').forEach(input => {
    input.addEventListener('mousedown', event => event.preventDefault());
});