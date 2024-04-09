import logo from './logo.svg';
import './Home.css';
import React, { useCallback, useEffect } from 'react'
import { useState } from 'react';
import { Alert, AppBar, Box, Button,  MenuItem, Select, Snackbar, TextField, Toolbar, Typography } from '@mui/material';
import UserCredentialsDialog from './UserCredentialsDialog/UserCredentialsDialog';
import { getUserToken,saveUserToken, clearUserToken } from "./localstorage";
import { DataGrid } from '@mui/x-data-grid';
import Nav from './Nav';
import { User } from './UserContext';
import Calculator from './Calculator';



function Home() {
  let [buyUsdRate, setBuyUsdRate] = useState(null);
 let [sellUsdRate, setSellUsdRate] = useState(null);
 let [lbpInput, setLbpInput] = useState("");
 let [usdInput, setUsdInput] = useState("");
 let [transactionType, setTransactionType] = useState(0);
 let [rateResult, setrateResult] = useState("");
 let [amountInput, setAmountInput] = useState("");

 

const {createUser}= User()

const {States}= User()
const {authState}= User()
const {setAuthState}= User()
const {userToken}= User()
const {SERVER_URL}= User()
const {login}= User()
const {logout}= User()
const {setLoginState}= User()
const {loginRejected}= User()
const {userTransactions}= User()
 

function fetchRates() {
  fetch(`${SERVER_URL}/api/exchangeRate`)
  .then(response => {
  return response.json();
})
  .then(data => {
  
    setBuyUsdRate( data['usd_to_lbp']);
    setSellUsdRate(data['lbp_to_usd']);

  });
 }

 
 
 function addItem() {
  
  var usd_amount = JSON.stringify(parseFloat(usdInput));
  var lbp_amount = JSON.stringify(parseFloat(lbpInput));
  var usdToLbp=transactionType
  

  fetch(`${SERVER_URL}/api/transaction`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${userToken}`,
    },
    body: JSON.stringify({
      "usd_amount": usd_amount,
      "lbp_amount": lbp_amount,
      "usd_to_lbp": usdToLbp
    })
  })
  .then(response => {
    return response.json();
  })
}

 useEffect(fetchRates, []);

  return (
    <div style={{}}>
    <head>
      <title>LBP Exchange Rate Tracker</title>

    </head>
    <body>
    <Nav/>

    
    
    
   <div className="wrapper">
        <h2>Today's Exchange Rate</h2>
        <p>LBP to USD Exchange Rate</p>
        <Typography variant="h5">Buy USD: <span id="buy-usd-rate">{buyUsdRate}</span></Typography>
        <Typography variant="h5">Sell USD: <span id="sell-usd-rate">{sellUsdRate}</span></Typography>
        <hr/>
        <Typography variant="h4">Record Transaction</Typography>
        <form name="transaction-entry" style={{display:'flex',flexDirection:'column'}}>
            <div className="amount-input">
                <label htmlFor="lbp-amount">LBP Amount</label>
                <input id="lbp-amount" type="number" value={lbpInput} onChange={e =>setLbpInput(e.target.value)}/>
               </div>
               <div className="amount-input">
                <label htmlFor="usd-amount">USD Amount</label>
                <input id="usd-amount" type="number" value={usdInput} onChange={e =>setUsdInput(e.target.value)}/>
               </div>

               <Select id="transaction-type" onChange={(e)=>{if (e.target.value==="usd-to-lbp"){setTransactionType(1)}
              else{setTransactionType(0)}}} >
                <MenuItem value="usd-to-lbp" >USD to LBP</MenuItem>
                <MenuItem value="lbp-to-usd">LBP to USD</MenuItem>
               </Select>
               <button id="add-button" onClick={addItem}  className="button" type="button">Add</button>
        </form>
  
    </div>

    <Calculator/>
    
        <hr style={{ margin: '40px auto' }} />
        </body>
        {userToken && (
        <div className="wrapper">
          <Typography variant="h4">Your Transactions</Typography>
          {userTransactions  && (
            <DataGrid
              columns={[
                { field: 'id', headerName: 'ID' },
                { field: 'usd_amount', headerName: 'USD Amount' },
                { field: 'lbp_amount', headerName: 'LBP Amount' },
                { field: 'usd_to_lbp', headerName: 'Trans Type' },
                { field: 'added_date', headerName: 'Date' },
                { field: 'user_id', headerName: 'MyID' }]}
              rows={userTransactions}
              autoHeight
            />
          )}
        </div>
      )}

      
    </div>
  );
}

export default Home;
