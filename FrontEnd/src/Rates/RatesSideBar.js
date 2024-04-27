import React, { useEffect, useState } from 'react';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import Drawer from '@mui/material/Drawer';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';
import { makeStyles } from '@mui/styles';
import { Box, Button, MenuItem, Select, Stack, TextField, Typography } from '@mui/material';
import './RatesSideBar.css'
import '../App.css'
import { User } from '../UserContext';


function RatesSideBar() {

 const [isOpen, setIsOpen] = useState(true);
 let [rateResult, setrateResult] = useState("");
 let [amountInput, setAmountInput] = useState("");
 let [conversionType, setConversionType] = useState(1);
  const toggleDrawer = () => {
    setIsOpen(!isOpen);
  };

  const {buyUsdRate}=User()
  const {sellUsdRate}=User()
  const {fetchRates}=User()

  useEffect(fetchRates,[])
  const handleCalculate = (e) => {
    e.preventDefault();
    const rate = conversionType === 1 ? sellUsdRate : buyUsdRate;
    const result = conversionType === 1 ? rate * amountInput : amountInput / rate;
    setrateResult(Math.round(100 * result) / 100);
  };

  return (
    <div>
           
<Drawer sx={{color:'white', height:'700px'}}  anchor="right" open={isOpen} onClose= {() => setIsOpen(false)}>

<Stack  sx={{width:'50vw' ,display:'flex', bgcolor:'#00000',height:'100%'}}>
      <Box sx={{display:'flex',flexDirection:'column',justifyContent:'space-between',height:'30%'}}>
          
          <Box className='headerDrawer'>
            <Typography class='headerText' >Today's Exchange Rate</Typography>
            </Box>

          <Box sx={{height:'70%',display:'flex',flexDirection:'column',justifyContent:'space-around',paddingLeft:'5%'}}>
            <Typography>Buy USD: <span id="buy-usd-rate">{buyUsdRate||""}</span></Typography>
            <Typography >Sell USD: <span id="sell-usd-rate">{sellUsdRate|| ""}</span></Typography>
          </Box>
        </Box>
        {/* <hr style={{margin:'20px'}}></hr> */}

        <Box sx={{height:'70%'}}>
        <Box id="calculatorHeader"  className='headerDrawer'>
        <Typography class='headerText' >Rate Calculator</Typography>
        </Box>
        <Box sx={{display:'flex',flexDirection:'column',justifyContent:'space-around',height:'80%',paddingLeft:'5%'}}>
          <Select 
            className='formField'
            id="conversion-type"
            value={conversionType}
            onChange={(e) => {
              setConversionType(e.target.value);
              setrateResult("");
            }}
          >









            
            <MenuItem value={1}>USD to LBP</MenuItem>
            <MenuItem value={0}>LBP to USD</MenuItem>
          </Select>
      
          <form style={{height:'65%',display:'flex',flexDirection:'column',justifyContent:'space-around'}} onSubmit={(e)=>{handleCalculate(e)}}>

            <Box sx={{display:'flex',flexDirection:'column',justifyContent:'space-around'}}>
              <Typography  sx={{marginBottom:'2%'}}>
                {conversionType === 1
                  ? "Amount in USD"
                  : "Amount in LBP"}
              </Typography>
              <TextField
                className='formField'
                type="number"
                value={amountInput}
                required
                onChange={(e) => setAmountInput(e.target.value)}
              />
            </Box>
            <Typography  >
              {conversionType === 1
                ? `Amount in LBP: ${rateResult} L.L`
                : `Amount in USD: ${rateResult} $`
              }
             
            </Typography>
                
            <Button
              id="calculate-button"
              class='formButton'
              variant="contained"
             
              type="submit"
            >
              Calculate
            </Button>
          </form>
          </Box>
          </Box>
</Stack>


</Drawer>
          
    </div>
  );
}

export default RatesSideBar;
