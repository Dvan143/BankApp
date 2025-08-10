fetch('/bankapp/api/getUserInfo')
    .then(data => data.json())
    .then(data => {
        document.getElementById('myName').innerText = data.name ?? 'Not set';
        if(data.email) {
            document.getElementById('myEmail').innerText = data.email
        } else {
            document.getElementById('myEmail').innerText = 'Not set'
        }
        document.getElementById('myBirthdayDate').innerText = data.birthdayDate ?? 'Not set';
        document.getElementById('myUsdAccount').innerText = data.usdAccount;
        document.getElementById('myGbpAccount').innerText = data.gbpAccount;
        document.getElementById('myJpyAccount').innerText = data.jpyAccount;
        document.getElementById('myRubAccount').innerText = data.rubAccount;
    })

function showChangingNameForm() {
    const div = document.getElementById('change-name-form');
    const button = document.getElementById('buttonChangeName')
    if(div.style.display==='none') {
        div.style.display='inline';
        button.innerText='Hide'
    } else {
        button.innerText='Change'
        div.style.display='none'
    }
}
function showChangingBirthdayForm() {
    const div = document.getElementById('change-birthday-form');
    const button = document.getElementById('buttonChangeBirthday')
    if(div.style.display==='none') {
        div.style.display='inline';
        button.innerText='Hide'
    } else {
        div.style.display='none'
        button.innerText='Change'
    }
}
function showChangingEmailForm() {
    const div = document.getElementById('change-email-form');
    const button = document.getElementById('buttonChangeEmail')
    if(div.style.display==='none') {
        div.style.display='inline';
        button.innerText='Hide'
    } else {
        div.style.display='none'
        button.innerText='Change'
    }
}

function sendEmailVerification() {
    const to = document.getElementById('email').value;
    const statusAnswer = document.getElementById('status');
    fetch('/bankapp/api/changeEmail', {
        method: 'POST',
        body: JSON.stringify({email:to}),
        headers: {
            "Content-Type": "application/json"
        },
        credentials: 'include'
    })
        .then(resp => resp.json())
        .then(resp => {
            resp = resp.status;
            if(resp==='ok') {
                const answer = document.getElementById('message-sent-answer')
                answer.style.display='inline'
            } else if(resp ==='exist') {
                statusAnswer.innerText='Email is exist';
            }
        })
}
function checkCode() {
    const email = document.getElementById('email').value;
    const code = document.getElementById('verifCode').value;
    fetch('/bankapp/api/checkVerificationCode', {
        method: 'POST',
        body: JSON.stringify({verificationCode: code, email: email}),
        headers: {
            "content-type": "application/json"
        }
    })
        .then(resp => resp.text())
        .then(resp => {
            resp=resp.status;
            if(resp==='ok'){
                location.reload();
            } else if (resp==='wrong') {
                document.getElementById('statusCode').innerText=`Wrong code.`;
                setTimeout(() => location.reload(), 2000)
            } else {
                location.href='error';
            }
        })
}