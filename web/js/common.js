/**
 * 校验登录表单
 * @param form
 * @returns {boolean}
 */
function check(form) {
    var formData = new FormData(form);
    var pwd = document.getElementById("pwd");
    var pwd_md5 = document.getElementById("pwd_md5");
    pwd_md5.value = hex_md5(pwd.value);
    console.log(checkEmail(formData.get("email")));
    return true;
}

/**
 * 校验注册表单
 * @param form
 * @returns {boolean}
 */
function signUpCheck(form) {
    var formData = new FormData(form);
    var pwd = document.getElementById("pwd");
    var pwd_md5 = document.getElementById("pwd_md5");

    var name = formData.get("name");
    var email = formData.get("email");
    if (isEmpty(name) || email == null || pwd.value == null ||
        !checkEmail(email) || !checkPwd(pwd.value)) {
        return false;
    }
    pwd_md5.value = hex_md5(pwd.value);
    console.log("sin_up success");
    return true;
}

function checkBook(form) {
    let formData = new FormData(form);
    let b=checkEmpty(formData);
    if(b)
    {
        if(checkPhone(formData.get("phone")))
        {
            return true;
        }else
        {
            alert("请输入有效手机号码");
            return false;
        }
    }else {
        alert("不能为空");
        return false;
    }


}
/**
 * 校验表单是否为空
 * @param form
 * @returns {boolean}
 */
function checkEmpty(formData) {
    for (let value of formData.values()) {
        if (isEmpty(value)) {
            return false;
        }
    }
    return true;
}

function isEmpty(s) {
    return (s == null || s == "");
}

/**
 * 向服务器查询邮箱是否已注册
 * @param email
 */
function checkEmailAvailable(email) {
    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            let email = document.getElementById("input_email");
            if ((xhr.status >= 200 && xhr.status < 300) || xhr.status == 304) {
                let jsonObj = JSON.parse(xhr.responseText);
                if (jsonObj.status == "ok") {
                    console.log(jsonObj.data);
                    let tip = document.getElementById("email_error");
                    slide(tip, "50px", !jsonObj.data);
                    setRedBorder(email, jsonObj.data);
                    return;
                }
            }
            setRedBorder(email, false);
            alert("Request was unsuccessful:" + xhr.status);
        }
    };
    xhr.open("POST", "/checkemail", true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.send("email=" + email);
}

function setName(name) {
    setRedBorder(name, name.value != null && name.value !== "")

}

function setPwd() {
    var input = document.getElementById("pwd");
    setRedBorder(input, input.value != null && checkPwd(input.value));
}

/**
 * 密码长度为8-16位，必须包含数字和字母，不能含有空格
 * @param pwd
 * @returns {boolean}
 */
function checkPwd(pwd) {
    if (pwd.length >= 8 && pwd.length <= 16) {
        if (!pwd.match(" ") && pwd.match(/[0-9]/) && pwd.match(/[a-zA-Z]/)) {

            return true;
        }
    }
    return false;

}

/**
 * 设置红色外边框
 * @param element
 * @param b
 */
function setRedBorder(element, b) {
    if (b) {
        element.setAttribute("class", "input_text")
    } else {
        element.setAttribute("class", "input_text_error")
    }
}

function setEmail(e) {
    setRedBorder(e, e.value != null && checkEmail(e.value));
}

/**
 * 检查Email有效性
 * @param s
 * @returns {boolean}
 */
function checkEmail(s) {
    const re = /^([a-z0-9A-Z]+[-|\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\.)+[a-zA-Z]{2,}$/;
    if (re.test(s)) {
        checkEmailAvailable(s);
        return true;
    } else {
        return false;
    }

}

function setPhone(phoneInput) {
    let error_p=phoneInput.nextElementSibling;
    let b=checkPhone(phoneInput.value);
    setRedBorder(phoneInput,b);
    if (b) {
        error_p.style.display="none";
    } else {
        error_p.style.display="inline";
    }
}
/**
 * 检查手机号的有效性
 * @param phone
 * @returns {boolean}
 */
function checkPhone(phone) {
    if (isEmpty(phone))
        return false;
    const re = /^((13[0-9])|(14[5-9])|(15[0-3,5-9])|(16[2,5,6,7])|(17[0-8])|(18[0-9])|(19[1,3,5,8,9]))\d{8}$/
    return re.test(phone);
}

/**
 * 向下滑动动画
 * @param element
 * @param height 元素高度
 * @param isShow 是否显示
 */
function slide(element, height, isShow) {
    if (isShow) {
        console.log("true 1");
        element.style.height = height;
        element.style.opacity = "1";
    } else {
        element.style.height = "0";
        element.style.opacity = "0";
    }
}

/**
 * 是否显示密码提示信息
 * @param isShow
 */
function tip(isShow) {
    const tip = document.getElementById("tip");
    slide(tip, "70px", isShow);
    if (!isShow)
        setPwd()
}

/**
 * 高亮搜索文字
 */
function highLight() {
    //DFS(document.body,"三体");
    const element = document.getElementById("right");
    const s = document.getElementById("search_input").value;
    console.log(s);
    if (s == null || s == "")
        return;
    const re = new RegExp("(>[^<]*)(" + s + ")(.*?<)", "g");
    console.log(re);
    element.innerHTML = element.innerHTML.replace(re, "$1<mark>$2</mark>$3");
}

/**
 * 上传文件
 */
function uploadFile() {
    let file = document.getElementById("upload_file");
    if (isEmpty(file.value)) {
        alert("请选择文件");
        return;
    }

    let form = document.getElementById("update_form");
    let formData = new FormData(form);
    let p = document.getElementById("update_tip");
    let input = document.getElementById("input_cover");
    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            if (xhr.responseText != null || xhr.responseText != "") {
                obj = JSON.parse(xhr.responseText);
                if (typeof obj == "object" && obj.status == "ok") {
                    p.innerText = "上传成功";
                    input.value = obj.data;
                } else {
                    p.innerText = "上传失败";
                }

            } else {
                p.innerText = "上传失败";
            }
            p.style.display = "block";
        }
    };
    xhr.open("POST", "/upload/cover", true);
    xhr.send(formData);
}

/**
 * 删除图书
 * @param a  a标签
 * @param id  图书ID
 */
function deleteBook(a, id) {
    if (!confirm("你确定要删除吗")) {
        return;
    }
    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            if (xhr.responseText != null || xhr.responseText != "") {
                obj = JSON.parse(xhr.responseText);
                console.log(xhr.responseText);
                if (typeof obj == "object" && obj.status == "ok") {
                    if (obj.data) {
                        let li = a.parentElement.parentElement.parentElement;
                        li.parentElement.removeChild(li);
                        return;
                    }
                }
            }
            alert("删除失败");
        }
    };
    xhr.open("GET", "/delete?id=" + id, true);
    xhr.send(null);
}
