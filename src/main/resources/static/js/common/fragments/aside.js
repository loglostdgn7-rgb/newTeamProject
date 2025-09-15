const asideNavs = document.querySelectorAll("aside > ul > li");
const asideCurrentPath = window.location.pathname;

asideNavs.forEach(asideButton => {
    const asideATag = asideButton.querySelector("a");

    // console.log(asideATag)

    if (asideATag && asideCurrentPath.includes(asideATag.getAttribute("href"))) {
        asideButton.classList.add("selected")
    }
});