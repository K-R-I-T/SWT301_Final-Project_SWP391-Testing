const send = async (method, url, body) => {
    try {
        const response = await fetch(url, {
            method: method,
            headers: {
                "Content-Type": "application/json",
                "authtoken": localStorage.getItem("authtoken")
            },
            body: body ? JSON.stringify(body) : undefined
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message);
        }
        console.log(response);
        if (response.status === 204)
            return;
        const data = await response.json(); // Parse the response as text

        return data; // Return the response as a string
    } catch (error) {
        console.error(error);
        throw error;
    }
};

const getAccountInfo = async () => {
    const token = localStorage.getItem("authtoken");
    if (token) {
        const userInfo = await send('GET', 'http://localhost:8080/OnlyArts/api/v4/user');
        const user = document.querySelector('#user');
        user.innerHTML = `${userInfo.lastName} ${userInfo.firstName}`;
        document.querySelector('#login').style.display = "none";
        document.querySelector('#logout').style.display = "block";
    } else {
        document.querySelector('#user').style.display = "none";
        document.querySelector('#login').style.display = "block";
        document.querySelector('#logout').style.display = "none";
    }
};
getAccountInfo();


const logOut = async () => {
    await send('PUT', 'http://localhost:8080/OnlyArts/api/v1/authentication/logout');
    localStorage.removeItem("authtoken");
    location.reload();
};
document.querySelector('#logout').addEventListener('onClick', async event => {

});



