import React, { useCallback, useEffect, useState } from 'react';
import { Box, Button, Dialog, DialogActions, Typography } from '@mui/material';
import Nav from '../Nav';
import { User } from '../UserContext';
import { DataGrid } from '@mui/x-data-grid';
import CreateOffer from './CreateOffer';

function AvailableOffers() {
  
  const { userToken, SERVER_URL, buyUsdRate, sellUsdRate } = User();
  const username = localStorage.getItem('username');
  const [offers, setOffers] = useState([]);
  const { setUserChatState, setChatName, setIsDrawerOpen } = User();
 const {setCantReachBackend}=User()

  const markComplete = () => {
    if (!navigator.onLine) {
        
      setCantReachBackend(true);
      return;
    }
    setDialogOpen(false);
    if (userToken)
    {
    fetch(`${SERVER_URL}/accept_offer/${selectedRow.id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${userToken}`,
      },
    })
    .then(response => response.json())
    .then(body => {
      fetchOffers();
    });
  }};

  const handleChat = () => {
    setDialogOpen(false);
    setIsDrawerOpen(true);
    setUserChatState(true);
    setChatName(selectedRow.username);
  };

  const calculateExchangeRate = (offer) => {
    const { amount_requested, amount_to_trade } = offer;
    let usd_to_lbp=offer["usd_to_lbp"]
    const rate = usd_to_lbp ? Math.abs(amount_requested/ amount_to_trade) : Math.abs(amount_to_trade / amount_requested);
    return rate
  };

  const fetchOffers = useCallback(() => {
    if (!navigator.onLine) {
        
      setCantReachBackend(true);
      return;
    }

    if (userToken)
    {
    fetch(`${SERVER_URL}/offers`, {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${userToken}`,
      },
    })
    .then((response) => response.json())
    .then((offers) => {
      console.log('here');
      const updatedOffers = offers.map(offer => ({
        ...offer,
        exchangeRate: calculateExchangeRate(offer),
      }));
      setOffers(updatedOffers);
    });
}}, [SERVER_URL, userToken, buyUsdRate, sellUsdRate]);

  const allOtherOffers = offers.filter(o => o.username !== username);

  useEffect(() => {
    fetchOffers();
  }, [fetchOffers]);

  const [selectedRow, setSelectedRow] = useState(null);
  const [dialogOpen, setDialogOpen] = useState(false);

  const handleRowClick = (params) => {
    setSelectedRow(params.row);
    setDialogOpen(true);
  };

  return (
    <div>
      <Nav />
      <Box sx={{ paddingTop: '10%', width: '100%', height: '600px', display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'space-around' }}>
        <Typography className='title' sx={{ height: '100px' }}>OFFERS</Typography>
        <CreateOffer fetchOffers={fetchOffers} />
        <DataGrid sx={{ marginTop: '5%', maxWidth: '100%' }}
          columns={[
            { field: 'id', headerName: 'ID' },
            { field: 'username', headerName: 'Username' },
            { field: 'amount_requested', headerName: 'Amount Requested' },
            { field: 'amount_to_trade', headerName: 'Amount To Trade' },
            { field: 'exchangeRate', headerName: 'Exchange Rate' },
            { field: 'added_date', headerName: 'Date' },
          ]}
          rows={allOtherOffers}
          onRowClick={handleRowClick}
        />
      </Box>

      <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)}>
        <DialogActions sx={{ display: 'flex', flexDirection: 'column' }}>
          <Button onClick={markComplete}>
            Accept
          </Button>
          <Button onClick={handleChat}>
            Chat
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}

export default AvailableOffers;
