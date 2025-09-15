const product_add_form = document.querySelector(".product_add_form");
const product_add_btn = document.querySelector(".pd-add");
const product_list_btn = document.querySelector(".pd-list");

product_list_btn.onclick = event => {
    event.preventDefault();
    product_add_form.style.visibility = "hidden";
}

product_add_btn.onclick = event => {
    event.preventDefault();
    product_add_form.style.visibility = "visible";
}
