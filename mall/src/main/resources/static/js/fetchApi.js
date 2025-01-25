async function fetchGet({ url, headers, success, fail }) {
    try {
        showSpinner();

        const option = { method: "GET" };
        if (headers) {
            option.headers = headers;
        }

        const response = await fetch(url, option);

        hideSpinner();
        if (response.ok) {
            if (success) {
                success(response);
            }
        } else {
            if (fail) {
                fail(response);
            }
        }
    } catch (error) {
        hideSpinner();
        console.error("요청 오류:", error);
        alert("요청 중 오류가 발생했습니다.");
    }
}

async function fetchPost({ url, data, headers, success, fail, simpleForm }) {
    try {
        showSpinner();

        const option = { method: "POST" };
        if (headers) {
            option.headers = headers;
        } else {
            option.headers = {};
        }

        // form 전송
        if (simpleForm) {
            option.headers = {
                ...option.headers,
                "Content-Type": "application/x-www-form-urlencoded"
            };
            option.body = data;
        }
        // 멀티파티 폼
        else if (data instanceof FormData) {
            option.body = data;
        }
        // json 전송
        else {
            option.headers = {
                ...option.headers,
                "Content-Type": "application/json"
            };
            option.body = JSON.stringify(data);
        }

        const response = await fetch(url, option);

        hideSpinner();
        if (response.ok) {
            if (success) {
                success(response);
            }
        } else {
            if (fail) {
                fail(response);
            }
        }
    } catch (error) {
        hideSpinner();
        console.error("요청 오류:", error);
        alert("요청 중 오류가 발생했습니다.");
    }
}