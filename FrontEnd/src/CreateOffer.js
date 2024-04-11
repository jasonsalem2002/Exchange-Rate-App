import logo from './logo.svg';
import './App.css';
import React, { useCallback, useEffect } from 'react'
import { useState } from 'react';
import { Alert, AppBar, Box, Button, Dialog, DialogActions, DialogContent, DialogTitle, Link, MenuItem, Select, Snackbar, TextField, Toolbar, Typography } from '@mui/material';
import UserCredentialsDialog from './UserCredentialsDialog/UserCredentialsDialog';
import { getUserToken,saveUserToken, clearUserToken } from "./localstorage";
import Nav from './Nav';
import { User } from './UserContext';
import { DataGrid } from '@mui/x-data-grid';


function CreateOffer(fetchOffers) {

   const [dialogOpen, setDialogOpen] = useState(false);
   const [transactionType,setTransactionType]= useState();

   const [amountRequested,setAmountRequested]=  useState();
  const [amountToTrade,setAmountToTrade]= useState();

  const {SERVER_URL}= useState();
  const {userToken}= useState();
  function addOffer() {
  
    var amount_requested = JSON.stringify(parseFloat(amountRequested));
    var lbp_amount = JSON.stringify(parseFloat(amountToTrade));
    var usdToLbp=transactionType
  
    fetch(`${SERVER_URL}/api/transaction`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${userToken}`,
      },
      body: JSON.stringify({
        "amount_requested": amountRequested,
        "amount_to_trade": amountToTrade,
        "usd_to_lbp": transactionType
      })
    })
    .then(response => {
      return response.json();
    })
  }  
  



   return(
   <div>
    <Button sx={{backgroundColor:'#0093d5',color:'white'}} onClick={()=>setDialogOpen(true)}>Create Offer</Button>
    <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)}>
    <Box sx={{backgroundColor:'#0093d5',height:'86px',display:'flex',flexDirection:'column',justifyContent:'center',paddingLeft:'4%'}}><Typography fontWeight={'bold'} color={'white'} variant='h6'>Create Offer</Typography></Box>
    <form style={{height:'500px',width:'600px',display:'flex',flexDirection:'column',justifyContent:'space-around',paddingLeft:'4%'}}  >
            
            
            <Box sx={{height:'100px',display:'flex',flexDirection:'column',justifyContent:'space-around'}}>
            
            <TextField
              className='formField'
              label="Amount Requested"
              name="amount_requested"
              value={amountRequested}
              onChange={(e)=>{setAmountRequested(e.target.value)}}
            />
            </Box>

            <Box sx={{height:'100px',display:'flex',flexDirection:'column',justifyContent:'space-around'}}>

            <TextField
             className='formField'
              label="Amount To Trade"
              name="amount_to_trade"
              value={amountToTrade}
              onChange={(e)=>{setAmountToTrade(e.target.value)}}
            />
            </Box>

            <Box sx={{height:'100px',display:'flex',flexDirection:'column',justifyContent:'space-around'}}>
            <Select className='formField' defaultValue="usd-to-lbp" sx={{width:'50%', borderRadius:'5%'}} id="transaction-type" onChange={(e)=>{if (e.target.value==="usd-to-lbp"){setTransactionType(1)}
              else{setTransactionType(0)}}} >
                <MenuItem value="usd-to-lbp" >USD to LBP</MenuItem>
                <MenuItem value="lbp-to-usd">LBP to USD</MenuItem>
               </Select>
            </Box>

            <Button onClick={()=> addOffer()} style={{width:'80px',padding:'8px',backgroundColor:'#0093d5'}} variant="contained" color="primary" type="submit">
              Add
            </Button>

      </form>
</Dialog>
    </div>
  );
}

export default CreateOffer;
