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
window.addEventListener("click", event => {
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
                // startDateInput.value = formDate(startDate);
                // endDateInput.value = formDate(today);
                startDateInput.datepicker.setDate(formDate(startDate));
                endDateInput.datepicker.setDate(formDate(today));


                //달력글자 깜박거리기
                startDateInput.style.fontWeight = "bold";
                endDateInput.style.fontWeight = "bold";
                setTimeout(() => {
                    startDateInput.style.fontWeight = "400";
                    endDateInput.style.fontWeight = "400";
                }, 150)
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

    document.getElementById("select-period").value = "직접선택";
});

startDateInput.addEventListener("changeDate", event => {
    const selectedDate = event.detail.date;
    const endDay = endDateInput.datepicker.getDate();

    if (selectedDate > endDay) {
        alert("종료일보다 나중은 선택 할 수 없습니다!");
        startDateInput.datepicker.setDate(endDay);
    }

    document.getElementById("select-period").value = "직접선택";
});

// 드래그 방지 기능
document.querySelectorAll('.no-drag').forEach(input => {
    input.addEventListener('mousedown', event => event.preventDefault());
});

/*******************************/
const params = new URLSearchParams(window.location.search);
// 조회 버튼 클릭 이벤트
getHistoryBtn.onclick = () => {
    const startDate = document.getElementById("date-picker-start").value;
    const endDate = document.getElementById("date-picker-end").value;
    const statusText = document.getElementById("select-status").value;
    let status = "";

    //상태 옵션
    // data-status 값 찾기
    if (statusText && statusText !== "주문상태") {
        const statusOptions = document.querySelectorAll(".status-option a");
        for (const option of statusOptions) {
            if (option.textContent.trim() === statusText) {
                status = option.dataset.status;
                break;
            }
        }
    }

    if (status === "ALL") status = "";

    //기간 옵션
    const periodText = document.getElementById("select-period").value;
    let period = "";

    if (periodText) {
        const periodOptions = document.querySelectorAll(".period-option a");
        for (const option of periodOptions) {
            if (option.textContent.trim() === periodText) {
                period = option.dataset.period;
                break;
            }
        }
    }

    if (period === "")

        // URL 파라미터
        params.set("startDate", startDate);
    params.set("endDate", endDate);

    if (status) params.set("status", status);
    else params.delete("status");

    if (period) params.set("period", period);
    else params.delete("period");

    location.href = `${location.pathname}?${params.toString()}`;
};

////앞의 두개 드랍다운 선택 유지///
const periodInput = document.getElementById("select-period");
const statusInput = document.getElementById("select-status")

// 기간
const periodParam = params.get("period");
const periodOption = document.querySelector(`.period-option a[data-period=${periodParam}]`);

if (periodParam && periodOption) {
    periodInput.value = periodOption.textContent;
} else if (params.has("startDate")) periodInput.value = "직접선택";
else periodInput.value = "일주일";

//상태
const statusParam = params.get("status");
const statusOption = document.querySelector(`.status-option a[data-status=${statusParam}]`);

if (statusParam && statusOption) {
    statusInput.value = statusOption.textContent;
} else statusInput.value = "전체";


/************ 주문 상태 초기화 *******************/
const resetBtn = document.querySelector('.reset-orders-status');
const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content')

if (resetBtn) {
    resetBtn.addEventListener('click', () => {
        if (!confirm('주문 상태를 초기화 하시겠습니까?')) {
            return;
        }

        fetch('/user/my-page/orders/reset', {
            method: 'post',
            headers: {
                'Content-Type': 'application/json',
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
                location.reload(); // 페이지 새로고침
            })
            .catch(error => {
                console.error('Error:', error);
                alert('초기화 중 오류가 발생했습니다.');
            });
    });
}