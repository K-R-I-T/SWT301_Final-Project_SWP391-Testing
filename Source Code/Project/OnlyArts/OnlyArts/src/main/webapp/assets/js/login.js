import http from "./http.js";

const loginForm = document.querySelector(".login-form");
const loginErrorMessage = document.querySelector("#login-error-message");

loginForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  const formData = new FormData(loginForm);
  const email = formData.get("email");
  const password = formData.get("password");

  if (!email) {
    loginErrorMessage.textContent = "Email is required";
    return;
  }
  if (email.startsWith(" ")) {
    loginErrorMessage.textContent =
      "Email should not have space at the beginning";
  }
  // Validate email format
  if (!/@gmail\.com$/.test(email)) {
    loginErrorMessage.textContent = "Email must be a valid Gmail address";
  }
  // Check if email has character before "@gmail.com"
  if (!/^.+@gmail\.com$/.test(email)) {
    loginErrorMessage.textContent =
      "Email must have characters before '@gmail.com'";
  }
  if (!password) {
    loginErrorMessage.textContent = "Password is required";
    return;
  }
  const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z]).{8,}$/;
  if (password.length < 8) {
    throw new Error("Password must be at least 8 characters long");
  } else if (!passwordRegex.test(password)) {
    throw new Error(
      "Password should contain both uppercase and lowercase letters"
    );
  }
  try {
    const data = await http.send("POST", `/api/v1/authentication/login`, {
      email,
      password,
    });
    localStorage.setItem("authtoken", data);
    alert("Login successful");

    window.location.href = "home";
  } catch (error) {
    loginErrorMessage.textContent = error.message;
  }
});
