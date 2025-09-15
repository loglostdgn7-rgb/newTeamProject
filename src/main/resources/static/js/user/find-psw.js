// 간편인증
const authenticationBtn = document.querySelector(".authentication-btn");
const telPrevSelect = document.querySelector(".find-psw-tel-prev");
const telBodyInput = document.querySelector(".find-psw-tel-body");
const telTailInput = document.querySelector(".find-psw-tel-tail");
const realPhoneNumber = document.querySelector(".phone");
const authenticationCheckDiv = document.querySelector(".find-psw-authentication-check-div");
const authenticationKeyInput = document.querySelector(".authentication-key");
const idInput = document.querySelector(".find-psw-id-input");
const findPswForm = document.querySelector(".find-psw-container");


// 간편인증 버튼 눌렀을 때
authenticationBtn.onclick = () => {

    realPhoneNumber.value = telPrevSelect.value + telBodyInput.value + telTailInput.value;

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
                telPrevSelect.disabled = true;
                telBodyInput.disabled = true;
                telTailInput.disabled = true;
                authenticationKeyInput.value = rsp["imp_uid"];
                authenticationBtn.textContent = "인증성공";
                authenticationBtn.disabled = true;
                authenticationCheckDiv.style.display = "none";
                console.log("인증 성공");

            } else {
                authenticationKeyInput.value = "";
                authenticationCheckDiv.style.display = "block";
                console.log("인증 실패");
            }
        },
    );
}


// 비밀번호 찾기 check 눌렀을때
findPswForm.addEventListener("submit", function (event) {
    event.preventDefault();//fetch 할거니까 submit 중단!

    const token = document.querySelector("meta[name='_csrf']").getAttribute("content");
    const header = document.querySelector("meta[name='_csrf_header']").getAttribute("content");
    const authenticationKey = authenticationKeyInput.value;

    // 유저
    const userId = idInput.value;
    const phoneNumber = realPhoneNumber.value;


    if (!/^[a-z][a-zA-Z0-9_-]{4,20}$/g.test(userId)) {
        alert("아이디를 정확히 입력해 주세요. 특수문자는 \"-\"와 \"_\"만 가능합니다");
        return;
    }

    // 간편인증이안되서 키가 없다
    if (authenticationKey === "") {
        alert("간편 인증을 해주세요");
        return;
    }

    fetch(`/user/find-psw`, {
        method: "post",
        headers: {
            "Content-Type": "application/json",
            [header]: token
        },
        body: JSON.stringify({
            id: userId,
            tel: phoneNumber,
            authenticationKey: authenticationKey
        })
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            }
            throw Error("서버 응답 에러");
        })
        .then(isSuccess => {
            if (isSuccess) {
                alert("인증 성공! 비밀번호 재설정 페이지로 이동합니다");
                window.location.href = "/user/reset-psw";
            } else {
                alert("입력하신 정보와 일치하는 사용자가 없습니다. 아이디와 휴대폰 번호를 다시 확인해 주세요");
            }
        }).catch(error => {
        console.error("Fetch Error: " + error);
    });


});
