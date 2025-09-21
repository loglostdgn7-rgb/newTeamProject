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
