const signupForm = document.querySelector('#signup-form');
const signupErrorMessage = document.querySelector('#signup-error-message');
signupForm.addEventListener('submit', event => {
    event.preventDefault();
    try {
        const firstname = document.querySelector('#firstname').value;
        const lastname = document.querySelector('#lastname').value;
        const email = document.querySelector('#email').value;
        const password = document.querySelector('#password').value;
        const repassword = document.querySelector('#repassword').value;
        const role = document.querySelector('#myDropdown').value;
        if (password !== repassword)
            throw new Error("Password does not match");
        const data = {
            "firstname": firstname,
            "lastname": lastname,
            "email": email,
            "password": password,
            "role": role
        };
        const token = send('POST',
                'http://localhost:8080/OnlyArts/api/v1/authentication/register',
                data);
    } catch (error) {
        error = error.toString().replace('Error: ', '');
        signupErrorMessage.innerHTML = `${error}`;
    }
});

