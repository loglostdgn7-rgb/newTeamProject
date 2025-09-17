// 아이디 중복확인
const checkIdDuplicated = document.querySelector(".authentication-id");
const idInput = document.querySelector(".id-input-container input");
let isDuplicated = true;

// 간편인증
const authenticationBtn = document.querySelector(".authentication-container > button");
const telPrev = document.querySelector(".tel-prev");
const telBody = document.querySelector(".tel-body");
const realPhoneNumber = document.querySelector(".phone");
const authenticationKeyInput = document.querySelector(".authentication-key");
const authenticationCheckSpan = document.querySelector(".authentication-check");

// 회원가입
const signupForm = document.querySelector(".signupForm");
const passwordInput = document.querySelector(".password");
const passwordReInput = document.querySelector(".password-re");

// 동의 체크 박스
const checkBoxes = document.querySelectorAll(".checkbox-container input[type='checkbox']");

// 서버 메세지
const serverMessageDiv = document.querySelector(".serve-message");

if (serverMessageDiv) {
    const message = serverMessageDiv.getAttribute("data-message");
    alert(message);
}

// 아이디 중복환인 버튼 눌렀을때
checkIdDuplicated.onclick = () => {
    const userId = idInput.value;
    if (!/^[a-z][a-zA-Z0-9_-]{4,20}$/g.test(userId)) {
        alert("아이디를 정확히 입력해 주세요. 특수문자는 \"-\"와 \"_\"만 가능합니다");
        return;
    }
    fetch(`/user/duplicated?id=${userId}`)
        .then(response => {
            if (response.ok && response.status === 200) {
                return response.json();
            } else {
                alert("아이디 중복 찾기에 실패 하였습니다. 다시 시도해 주세요");
                throw Error("서버 응답 에러");
            }
        })
        .then(value => {
            if (value) {
                alert("이미 사용중인 아이디 입니다");
                isDuplicated = true;// 중복이다
            } else {
                alert("사용 가능한 아이디 입니다");
                isDuplicated = false //중복 아니다
            }
        })
        .catch(error => {
            console.error("회원 가집 도중 에러 발생: " + error);
            alert("회원 가집 도중 에러 발생 했습니다. 다시 시도해 주세요");
        });
}

// 간편인증 버튼 눌렀을 때
authenticationBtn.onclick = () => {
    realPhoneNumber.value = telPrev.value + telBody.value;
    if (!/^01([0|1|6|7|8|9])([0-9]{3,4})([0-9]{4})$/g.test(realPhoneNumber.value)) {
        alert("휴대폰 번호를 정확히 입력해주세요");
        return;
    }

    IMP.init("imp76108135");
    IMP.certification(
        {
            channelKey: "channel-key-bb87913f-7fa3-4f4d-a293-49776bfb989f"
        },
        function (rsp) {
            console.log("response: ", rsp);
            if (rsp.success) {
                telPrev.disabled = true;
                telBody.disabled = true;
                authenticationKeyInput.value = rsp["imp_uid"];
                authenticationBtn.textContent = "인증성공";
                authenticationBtn.disabled = true;
                authenticationCheckSpan.style.display = "none";
                console.log("인증 성공");

            } else {
                authenticationKeyInput.value = "";
                authenticationCheckSpan.style.display = "block";
                console.log("인증 실패");
            }
        },
    );
}

// 회원가입 눌렀을때
signupForm.addEventListener("submit", function (event) {
    const password = passwordInput.value;
    const passwordRe = passwordReInput.value;
    // 비밀번호
    if (!/^(?:(?=.*[A-Za-z])(?=.*[0-9])|(?=.*[A-Za-z])(?=.*[!@#$%^&*()_-])|(?=.*[0-9])(?=.*[!@#$%^&*()_-]))[A-Za-z0-9!@#$%^&*()_-]{4,60}$/g.test(password)) {
        event.preventDefault();
        alert("정상적인 비밀번호가 아닙니다. 다시 시도해 주세요");
        return;
    }
    if (password !== passwordRe) {
        event.preventDefault();
        alert("비밀번호가 다릅니다. 다시 시도해 주세요");
        return;
    }

    // 중복체크
    if (isDuplicated) {
        event.preventDefault();
        alert("아이디 중복 체크를 해주세요");
        return;
    }

    // 간편인증이안되서 키가 없다
    const authenticationKey = authenticationKeyInput.value;
    if (authenticationKey === "") {
        event.preventDefault();
        alert("간편 인증을 해주세요");
        return;
    }

    for (const checkBox of checkBoxes) {
        if (!checkBox.checked) {
            event.preventDefault();
            alert("이용약관과 개인정보수집 및 이용을 동의해 주세요");
            return;
        }
    }

});
