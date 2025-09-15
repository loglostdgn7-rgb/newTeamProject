// 우편/주소 검색
function searchAddress() {
    new daum.Postcode({
        oncomplete: function (data) {
            let roadAddress = '';

            // 사용자가 도로명 주소를 선택했는지, 지번 주소를 선택했는지 확인합니다.
            if (data.userSelectedType === 'R') {
                // 도로명 주소를 선택한 경우, 도로명 주소를 사용합니다.
                roadAddress = data.roadAddress;
            } else {
                // 지번 주소를 선택한 경우(J), 지번 주소를 사용합니다.
                roadAddress = data.jibunAddress;
            }
            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('sample4_postcode').value = data.zonecode;
            document.getElementById("sample4_roadAddress").value = roadAddress;
            document.getElementById("sample4_detailAddress").focus();
        }
    }).open();
}

// [alert] server-message //////////////
const serverMessageSection = document.querySelector(".profile-section");
const message = serverMessageSection.getAttribute("data-message");

if (serverMessageSection && message != null) {
    alert(message)
}

// 프로필 수정 Form ////////////////
const profileForm = document.querySelector(".profile-container");

profileForm.onsubmit = event => {
    const passwordInput = document.querySelector(".password");
    const passwordReInput = document.querySelector(".password-re");
    const password = passwordInput.value;
    const passwordRe = passwordReInput.value;

    if (password !== "") {
        if (!/^(?:(?=.*[A-Za-z])(?=.*[0-9])|(?=.*[A-Za-z])(?=.*[!@#$%^&*()_-])|(?=.*[0-9])(?=.*[!@#$%^&*()_-]))[A-Za-z0-9!@#$%^&*()_-]{4,60}$/g) {
            event.preventDefault();
            alert("정상적인 비밀번호가 아닙니다. 다시 시도해 주세요\n(영문 대소문자/숫자/특수문자 중 2가지 이상 조합, 4자~60자)");
            return;
        }

        if (password !== passwordRe) {
            event.preventDefault();
            alert("비밀번호가 다릅니다. 다시 시도해 주세요");
            return;
        }
    }

    const nameInput = document.querySelector(".user-name");
    const name = nameInput.value;

    if (name !== "") {
        if (!/^[가-힣]{2,4}$/g.test(name)) {
            event.preventDefault();
            alert("이름은 한글 2-4자만 가능합니다");
            return;
        }
    }

    const postCode = document.getElementById("sample4_postcode").value;
    const roadAddress = document.getElementById("sample4_roadAddress").value;
    const detailAddress = document.getElementById("sample4_detailAddress").value;

    if (postCode !== "") {
        if (!/\d{5}(-\d{3})?/g.test(postCode)) {
            event.preventDefault();
            alert("우편번호가 정상적이지 않습니다. 다시 시도해 주세요");
            return;
        }
        if (roadAddress === "") {
            event.preventDefault();

            alert("선택된 주소가 전달되지 않았습니다. 다시 시도해 주세요");
            return;
        }
        if (detailAddress === "") {
            event.preventDefault();
            alert("상세 주소를 입력해 주세요");
            return;
        }
    }
}

/*********************************/
const snsLinkBtns = document.querySelectorAll(".sns-connect-container button");
snsLinkBtns.forEach(btn => {
    btn.onclick = () => {
        if (btn.disabled) {
            return;
        }

        const clientName = btn.id.toLowerCase();
        switch (clientName) {
            case "kakao":
                sns_link_request(
                    "https://kauth.kakao.com/oauth/authorize",
                    "a81aac1853d61ba221c90fb237b6238b",
                    clientName
                );
                break;
            case "naver":
                sns_link_request(
                    "https://nid.naver.com/oauth2.0/authorize",
                    "OotspZMgxoac95UFeF9I",
                    clientName
                );
                break;
        }
    }
})

function sns_link_request(AUTH_ENDPOINT, CLIENT_ID, CLIENT_NAME) {
    const RESPONSE_TYPE = "code";
    const REDIRECT_URI = `http://localhost:8080/user/my-page/oauth2/${CLIENT_NAME}`;

    const request_url = `${AUTH_ENDPOINT}?` +
        `response_type=${RESPONSE_TYPE}&` +
        `client_id=${CLIENT_ID}&` +
        `redirect_uri=${REDIRECT_URI}`;

    location.href = request_url;

    console.log("다 작동했다");
}