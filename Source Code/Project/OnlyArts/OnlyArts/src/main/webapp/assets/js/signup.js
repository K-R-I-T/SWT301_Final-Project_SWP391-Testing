import http from "./http.js";

const signupForm = document.querySelector("#signup-form");
const signupErrorMessage = document.querySelector("#signup-error-message");
signupForm.addEventListener("submit", async (event) => {
  event.preventDefault();

  try {
    const firstname = document.querySelector("#firstname").value;
    const lastname = document.querySelector("#lastname").value;
    const email = document.querySelector("#email").value;
    const password = document.querySelector("#password").value;
    const repassword = document.querySelector("#repassword").value;
    const role = document.querySelector("#myDropdown").value;

    //check first name

    // Check first name
    if (!firstname) {
      throw new Error("Firstname is required");
    } else if (!isNaN(firstname)) {
      throw new Error("First name should not contain numbers");
    } else if (/[^\w\s]/.test(firstname)) {
      throw new Error("First name should not contain special characters");
    }

    // Check last name
    if (!lastname) {
      throw new Error("Last name is required");
    } else if (!isNaN(lastname)) {
      throw new Error("Last name should not contain numbers");
    } else if (/[^\w\s]/.test(lastname)) {
      throw new Error("Last name should not contain special characters");
    }

    if (!email) {
      throw new Error("Email is required");
    }
    // Check if email has space at the beginning
    if (email.startsWith(" ")) {
      throw new Error("Email should not have space at the beginning");
    }
    // Validate email format
    if (!/@gmail\.com$/.test(email)) {
      throw new Error("Email must be a valid Gmail address");
    }
    // Check if email has character before "@gmail.com"
    if (!/^.+@gmail\.com$/.test(email)) {
      throw new Error("Email must have characters before '@gmail.com'");
    }
    // Check if password matches
    if (!password) {
      throw new Error("Password is required");
    }
    if (password !== repassword) {
      throw new Error("Password does not match");
    }
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z]).{8,}$/;
    if (password.length < 8) {
      throw new Error("Password must be at least 8 characters long");
    } else if (!passwordRegex.test(password)) {
      throw new Error(
        "Password should contain both uppercase and lowercase letters"
      );
    }

    if (!role) {
      throw new Error("Role is required");
    }
    const data = {
      firstName: `${firstname}`,
      lastName: `${lastname}`,
      email: `${email}`,
      password: `${password}`,
      roleId: `${role}`,
    };
    const token = await http.send(
      "POST",
      "/api/v1/authentication/register",
      data
    );
    console.log(token);
    localStorage.setItem("authtoken", `${token}`);
    console.log("User registered successfully");
    alert("User registered successfully");
    window.location.href = "http://localhost:8080/OnlyArts/index.html";
  } catch (error) {
    error = error.toString().replace("Error: ", "");
    console.log(error);
    signupErrorMessage.innerHTML = `${error}`;
  }
});
