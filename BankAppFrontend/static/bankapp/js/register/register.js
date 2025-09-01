function register() {
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;

    const answer = document.getElementById('answer');

    if(password===confirmPassword) {
        const username = document.getElementById('username').value
        const email = document.getElementById('email').value

        fetch('/bankapp/auth/register',{
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({username: username, email: email, password: password})
        })
            .then(resp => resp.json())
            .then(data => {
                console.log(data);
                if(data.status==='ok'){
                    window.location.replace('/profile');
                } else if(data.status==='bothExist') {
                    answer.innerText = 'Username and email are exist';
                } else if(data.status==='emailExist') {
                    answer.innerText = 'Email is exist';
                } else if(data.status==='usernameExist') {
                    answer.innerText = 'Username is exist';
                }
            })
    } else {
        answer.innerText='Password aren\'t same';
    }
}