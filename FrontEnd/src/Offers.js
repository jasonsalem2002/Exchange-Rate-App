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
import CreateOffer from './CreateOffer';



function Offers() {

    const rows = [
        { id: 1,username:'ymc09' ,amountRequested: 5000000, amountOffered:5,exchangeRate:100000 ,added_date: '2024-04-04'},
        { id: 2,username:'ymc06' ,amountRequested: 5, amountOffered:500000,exchangeRate:1000000 ,added_date: '2024-04-07' },
        { id: 3,username:'ymc04' ,amountRequested: 2, amountOffered:180000,exchangeRate:900000 ,added_date: '2024-04-03' },
        { id: 4,username:'ymc02' ,amountRequested: 5000000, amountOffered:5,exchangeRate:100000 ,added_date: '2024-04-01'},
        { id: 5,username:'ymc06' ,amountRequested: 8, amountOffered:760000,exchangeRate:950000 ,added_date: '2024-04-02' },
        { id: 6,username:'ymc08' ,amountRequested: 2000000, amountOffered:25,exchangeRate:80000 ,added_date: '2024-04-03' }
    ]

    const [selectedRow, setSelectedRow] = useState(null);
    const [dialogOpen, setDialogOpen] = useState(false);
    const handleRowClick = (params) => {
        setSelectedRow(params.row);
        setDialogOpen(true);
      };

      const handleAccept = () => {
        setDialogOpen(false);
      };
    
      const handleReject = () => {
        setDialogOpen(false);
      };

  return (
    <div style={{}}>
        <Nav/>
        
        <Box sx={{paddingTop:'10%', width:'100%',height:'600px',display:'flex',flexDirection:'column',alignItems:'center', justifyContent:'space-around'}}>
            {/* <Typography variant='h5' sx={{height:'100px'}}>OFFERS</Typography> */}
            <CreateOffer/>
        <DataGrid sx={{marginTop:'5%'}}
              columns={[
                { field: 'id', headerName: 'ID' },
                { field: 'username', headerName: 'Username' },
                { field: 'amountOffered', headerName: 'Amount Offered' },
                { field: 'amountRequested', headerName: 'Amount Requested' },
                { field: 'exchangeRate', headerName: 'Exchange Rate' },
                { field: 'added_date', headerName: 'Date' },
        
              ]}
              rows={rows}
              onRowClick={handleRowClick}
              autoHeight
            />
             </Box>

             <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)}>

        <DialogActions sx={{display:'flex',flexDirection:'column'}}>
          <Button onClick={handleAccept} >
            Accept
          </Button>
          <Button onClick={handleReject} >
            Reject
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}

export default Offers;
