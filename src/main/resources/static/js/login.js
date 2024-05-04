const handlesubmit = (event) => {
    const formData = new FormData(event.target);
    const formDataJSON = Object.fromEntries(formData.entries());

    fetch("http://localhost:8080/auth/signin", {
          method: "POST",
          headers: {
              "Content-Type": "application/json"
          },
          body: JSON.stringify(formDataJSON)
    })
    .then(data => {
        console.log(data);
    })
    .catch(error => console.log(error));
}

document.getElementById("userDetails").addEventListener("submit", handlesubmit);