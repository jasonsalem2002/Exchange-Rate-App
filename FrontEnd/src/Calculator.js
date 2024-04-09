import React, { useState } from 'react';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import Drawer from '@mui/material/Drawer';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';
import { makeStyles } from '@mui/styles';
import { Box, Button, MenuItem, Select, Stack, TextField, Typography } from '@mui/material';
import './Home.css'



function Calculator(sellUsdRate,buyUsdRate) {
 const [open, setOpen] = useState(false);
 let [rateResult, setrateResult] = useState("");
 let [amountInput, setAmountInput] = useState("");
 let [conversionType, setConversionType] = useState("");
  const toggleDrawer = () => {
    setOpen(!open);
  };

  const handleCalculate = () => {
    const rate = conversionType === 1 ? sellUsdRate : buyUsdRate;
    const result = conversionType === 1 ? rate * amountInput : amountInput / rate;
    setrateResult(Math.round(100 * result) / 100);
  };

  return (
    <div>
      <IconButton
        color="inherit"
        aria-label="menu"
        onClick={toggleDrawer}
        >
        <MenuIcon />
      </IconButton>
      <Drawer aria-label="muiDrawer"
        anchor="right"
        open={open}
        onClose={toggleDrawer}
       >
         <div className="calculator">
         <Stack spacing={2} sx={{width:'50vw', height:'50vh' ,display:'flex',alignItems:'center', bgcolor:'#d3d3d3'}}>
          <Typography variant="h4">Rate Calculator</Typography>
          <Select
            labelId="conversion-type"
            id="conversion-type"
            value={conversionType}
            onChange={(e) => {
              setConversionType(e.target.value);
              setrateResult("");
            }}
            style={{ fontSize: '15px', padding: '0px' }}
          >
            <MenuItem value={1}>USD to LBP</MenuItem>
            <MenuItem value={0}>LBP to USD</MenuItem>
          </Select>
          <br />
          <br />
          <form name="transaction-entry">
            <div className="amount-input">
              <Typography variant="h5" htmlFor="amount-input">
                {conversionType === 1
                  ? "Amount in USD"
                  : "Amount in LBP"}
              </Typography>
              <TextField
                id="amount-input"
                type="number"
                value={amountInput}
                onChange={(e) => setAmountInput(e.target.value)}
              />
            </div>
            <Typography variant="h5">
              {conversionType === 1
                ? "Amount in LBP"
                : "Amount in USD"} : {" "}
              <span id="rate-result">{rateResult}</span>
            </Typography>
            <br />         
            <Button
              id="calculate-button"
              variant="contained"
              color="primary"
              onClick={handleCalculate}
            >
              Calculate
            </Button>
          </form>
          </Stack>
        </div>
      </Drawer>
    </div>
  );
}


export default Calculator;
