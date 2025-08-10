fetch('/api/getUserInfo')
    .then(data => data.json())
    .then(data => {
        document.getElementById('myUsdAccount').innerText = data.usdAccount;
        document.getElementById('myGbpAccount').innerText = data.gbpAccount;
        document.getElementById('myJpyAccount').innerText = data.jpyAccount;
        document.getElementById('myRubAccount').innerText = data.rubAccount;
    })
function checkUsername() {
    let username = document.getElementById('usernameOfRecipient').value
    fetch('/api/transfer/userInfo', {
        method:"POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({"username":username})
    })
        .then(data => data.json())
        .then(userInfo => {
            const statusDiv = document.getElementById('status')
            if(userInfo.username != null) {
                statusDiv.innerHTML=`
                <div>Username of recipient: ${userInfo.username}</div>
                `
                showTransferBlock();
                if(userInfo.name!=null) {
                    const newBlock = document.createElement('div')
                    newBlock.innerHTML = `<div>Name of recipient: ${userInfo.name}</div>`;
                    statusDiv.appendChild(newBlock);
                }
            } else {
                statusDiv.innerText = 'User not exist'
            }
        })
}
function showTransferBlock() {
    const transferBlock = document.getElementById('transfer-block');
    transferBlock.style.display='inline';
}
function sendMoney() {
    let moneyAmount = document.getElementById('moneyAmount').value;
    let senderMessage = document.getElementById('senderMessage').value;
    let to = document.getElementById('usernameOfRecipient').value;
    let currency = document.getElementById('currency').value;
    fetch('/api/transfer/sendMoney', {
        method:'POST',
        body: JSON.stringify({"money":moneyAmount, "message":senderMessage, "to":to, "currency":currency}),
        headers: {
            'Content-Type':'application/json'
        }
    })
        .then(resp => resp.json())
        .then(resp => {
            const statusDiv = document.getElementById('money-status')
            if(resp.status==='ok') {
                statusDiv.innerText='Money has been sent';
            } else if(resp.status==='notEnoughMoney'){
                statusDiv.innerText='You have not enough money to do that transfer';
            } else {
                statusDiv.innerText='Error';
            }
        })
}