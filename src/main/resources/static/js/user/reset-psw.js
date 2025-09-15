const resetPswForm = document.querySelector("form.reset-psw-container");
const passwordInput = document.querySelector(".psw");
const passwordReInput = document.querySelector(".psw-re");


resetPswForm.onsubmit = event => {
    const password = passwordInput.value;
    const passwordRe = passwordReInput.value;

    if (!/^(?:(?=.*[A-Za-z])(?=.*[0-9])|(?=.*[A-Za-z])(?=.*[!@#$%^&*()_-])|(?=.*[0-9])(?=.*[!@#$%^&*()_-]))[A-Za-z0-9!@#$%^&*()_-]{4,60}$/g.test(password)){
        event.preventDefault();
        alert("정상적인 비밀번호가 아닙니다. 다시 시도해 주세요\n(영문 대소문자/숫자/특수문자 중 2가지 이상 조합, 4자~60자)");
        return;
    }

    if (password !== passwordRe){
        event.preventDefault();
        alert("비밀번호가 다릅니다. 다시 시도해 주세요");
        return;
    }


}