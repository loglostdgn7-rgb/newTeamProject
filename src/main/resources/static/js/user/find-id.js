// 간편인증
const authenticationBtn = document.querySelector(".authentication-btn");
const telPrevSelect = document.querySelector(".find-id-tel-prev");
const telBodyInput = document.querySelector(".find-id-tel-body");
const telTailInput = document.querySelector(".find-id-tel-tail");
const realPhoneNumber = document.querySelector(".phone");
const authenticationCheckDiv = document.querySelector(".find-id-authentication-check-div");
const authenticationKeyInput = document.querySelector(".authentication-key");
const findIdForm = document.querySelector(".find-id-container");

// 간편인증 눌렀을 때
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
                const authenticationBtnBox = document.querySelector(".find-id-authentication-button-box");
                authenticationBtnBox.style.marginBottom = "15.2px";

                console.log("인증성공");
            } else {
                authenticationKeyInput.value = "";
                authenticationCheckDiv.style.display = "block";
                console.log("인증실패");
            }
        },
    );
}

findIdForm.onsubmit = event => {
    event.preventDefault();//fetch 할거니까 submit 중단!

    const token = document.querySelector("meta[name='_csrf']").getAttribute("content");
    const header = document.querySelector("meta[name='_csrf_header']").getAttribute("content");
    const authenticationKey = authenticationKeyInput.value;

    // 유저
    const phoneNumber = realPhoneNumber.value;

    if (authenticationKey === "") {
        alert("간편 인증을 해주세요");
        return;
    }

    fetch(`/user/find-id`, {
        method: "post",
        headers: {
            "Content-Type": "application/json",
            [header]: token
        },
        body: JSON.stringify({
            tel: phoneNumber,
            authenticationKey: authenticationKey
        })
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                alert("아이디 찾기에 실패 했습니다. 다시 시도해 주세요");
                throw Error("서버 응답 에러");
            }
        })
        .then(data => {
            if (data && data.userId) {
                alert("인증성공! 아이디를 확인해 주세요");
                const findIdH1 = document.querySelector(".find-id-section > h1");
                findIdH1.style.display = "none";
                findIdForm.style.display = "none";
                const userIdDiv = document.querySelector(".user-id-div");
                userIdDiv.style.display = "flex";
                const userIdContent = document.querySelector(".user-id-content");
                userIdContent.textContent = data.userId;
            }
        })
        .catch(error => {
            console.error("아이디를 찾는 도중 에러 발생: " + error);
            alert("아이디를 찾는 도중 에러 발생했습니다. 다시 시도해 주세요");
        })
}
