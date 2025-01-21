async function fetchPost({ url, data, success, fail }) {
    try {
        showSpinner();

        const option = { method: "POST" }

        // form 전송
        if (data instanceof FormData) {
            option.body = data;
        }
        // json 전송
        else {
            option.headers = { "Content-Type": "application/json" };
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