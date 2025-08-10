fetch('https://open.er-api.com/v6/latest/USD')
    .then(data => data.json())
    .then(data => {
        document.getElementById('euro').innerText = parseFloat(data.rates.EUR).toFixed(2);
        document.getElementById('gbp').innerText = parseFloat(data.rates.GBP).toFixed(2);
        document.getElementById('jpy').innerText = parseFloat(data.rates.JPY).toFixed(2);
        document.getElementById('rub').innerText = parseFloat(data.rates.RUB).toFixed(2);
    })
