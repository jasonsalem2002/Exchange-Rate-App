import logo from './logo.svg';
import './App.css';
import './Home.css';
import React, { useCallback, useEffect } from 'react'
import { useState } from 'react';
import { Alert, AppBar, Box, Button,  Input,  MenuItem, Select, Snackbar, TextField, Toolbar, Typography } from '@mui/material';
import UserCredentialsDialog from './UserCredentialsDialog/UserCredentialsDialog';
import { getUserToken,saveUserToken, clearUserToken } from "./localstorage";
import { DataGrid } from '@mui/x-data-grid';
import Nav from './Nav';
import { User } from './UserContext';
import RatesSideBar from './RatesSideBar';
import ChatDrawer from './ChatDrawer';




function Home() {
 
 let [lbpInput, setLbpInput] = useState("");
 let [usdInput, setUsdInput] = useState("");
 let [transactionType, setTransactionType] = useState(false);
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
 



 
 
 function addItem() {
  
  var usd_amount = JSON.stringify(parseFloat(usdInput));
  var lbp_amount = JSON.stringify(parseFloat(lbpInput));
  var usdToLbp=transactionType
  

  fetch(`${SERVER_URL}/transaction`, {
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



  return (
    <div style={{}}>
    <head>
      <title>LBP Exchange Rate Tracker</title>

    </head>
    <body>
    <Nav/>

    <ChatDrawer/>
    

   <div className="wrapper">
      
        <Box className='header'>
        <Typography className='headerText' variant="h4">Record Transaction</Typography>
        </Box>
        <form  style={{height:'75%',display:'flex',flexDirection:'column',justifyContent:'space-around',paddingLeft:'5%'}}>
    
              <TextField  className='formField' label="LBP Amount" type="number" value={lbpInput} onChange={e =>setLbpInput(e.target.value)}/>
              <TextField className='formField' label="USD Amount" type="number" value={usdInput} onChange={e =>setUsdInput(e.target.value)}/> 
              <Select className='formField' defaultValue={"usd-to-lbp"} id="transaction-type" onChange={(e)=>{if (e.target.value==="usd-to-lbp"){setTransactionType(true)}
              else{setTransactionType(false)}}} >

                  <MenuItem value="usd-to-lbp" >USD to LBP</MenuItem>
                  <MenuItem value="lbp-to-usd">LBP to USD</MenuItem>
               </Select>
               <Button class='formButton'  onClick={addItem} type="button">Add</Button>
        </form>
  
    </div>

   
    
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
