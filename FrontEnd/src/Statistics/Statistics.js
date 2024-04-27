import React, { useCallback, useEffect, useState } from 'react';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend } from 'recharts';
import { User } from '../UserContext';
import { Box, Button, TextField, Select, MenuItem, FormControl, InputLabel, Typography,Snackbar,Alert } from '@mui/material';
import Nav from '../Nav';
import '../App.css';
import './Statistics.css';


function Statistics() {
  const [data, setData] = useState([]);
  const [xAxisData, setXAxisData] = useState([]);
  const [yAxisData, setYAxisData] = useState([]);
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [fetchedData, setFetchedData] = useState(false);
  const [granularity, setGranularity] = useState('daily');
  const { SERVER_URL } = User();
  const { userToken } = User();
  const [requirementsRejected,setRequirementsState]=useState(false)
 const [highestTransactionToday,setHighestTransactionToday]=useState()
 const [numberOfTransactions,setNumberOfTransactions]=useState([])
 const [volumeOfTransactions,setVolumeOfTransactions]=useState()
 const [largestTransactionAmount,setLargestTransactionAmount]=useState('')
 const{setCantReachBackend}=User()

 const handleStartDateChange = (date) => {
    setStartDate(date);
 
  };

  const handleEndDateChange = (dateString) => {
    const date = new Date(dateString);
    if (date < new Date(startDate)) {
      const endDateObj = new Date(startDate);
      endDateObj.setDate(endDateObj.getDate() + 1);
      const formattedEndDate = endDateObj.toISOString().slice(0, 10);
      setEndDate(formattedEndDate);
    } else {
      setEndDate(dateString);
    }
  };





  const fetchHighestTransactionToday = () => {
    if (!navigator.onLine) {
        
        setCantReachBackend(true);
        return;
      }
    if (userToken)
    {
    fetch(`${SERVER_URL}/highest_transaction_today`, {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${userToken}`,
      },
    })
      .then((response) =>{

        if (response.ok)
        {
            return response.json();
        }
        
       

    })
      .then((highestTransactionToday) => {
        setHighestTransactionToday(highestTransactionToday);
      });
  }};


  const fetchLargestTransactionAmount = () => {
    if (!navigator.onLine) {
        
        setCantReachBackend(true);
        return;
      }
    if (userToken){
    fetch(`${SERVER_URL}/largest_transaction_amount?start_date=${startDate}&end_date=${endDate}`, {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${userToken}`,
      },
    })
      .then((response) =>{

        if (response.ok)
        {
            return response.json();
        }
        
       

    })
      .then((largestTransactionAmount) => {
        setLargestTransactionAmount(largestTransactionAmount);
      });
  }};


  const fetchNumberOfTransactions = () => {
    if (!navigator.onLine) {
        
        setCantReachBackend(true);
        return;
      }
    if (userToken)
    {
    fetch(`${SERVER_URL}/number_of_transactions?start_date=${startDate}&end_date=${endDate}`, {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${userToken}`,
      },
    })
      .then((response) =>{

        if (response.ok)
        {
            return response.json();
        }
        
       

    })
      .then((numberOfTransactions) => {
        
        setFetchedData(true)
        setNumberOfTransactions(numberOfTransactions);
      });
  }};


  const fetchVolumeOfTransactions = () => {
    if (!navigator.onLine) {
        
        setCantReachBackend(true);
        return;
      }

    if (userToken){
    fetch(`${SERVER_URL}/volume_of_transactions?start_date=${startDate}&end_date=${endDate}`, {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${userToken}`,
      },
    })
      .then((response) =>{

        if (response.ok)
        {
            return response.json();
        }
        
       

    })
      .then((volumeOfTransactions) => {
        setVolumeOfTransactions(volumeOfTransactions);
      });
    }
  };

  useEffect(() => {
    if (fetchedData) {

        const transactionsData = numberOfTransactions.transactions_per_period;
        const dates = Object.keys(transactionsData);
        const counts = Object.values(transactionsData);
   
        setXAxisData(dates);
    
        setYAxisData(counts);

    }
  }, [data, fetchedData]);

  const handleSubmit = (e) => {
    e.preventDefault();
    fetchHighestTransactionToday();
    fetchLargestTransactionAmount();
    fetchNumberOfTransactions();
    fetchVolumeOfTransactions()
  };


  useEffect(() => {
    const startDateObj = new Date();
    startDateObj.setDate(startDateObj.getDate() - 30);
    const formattedStartDate = startDateObj.toISOString().slice(0, 10);
    const endDateObj = new Date(startDateObj); 
    endDateObj.setDate(endDateObj.getDate()+30);
    const formattedEndDate = endDateObj.toISOString().slice(0, 10);
    setStartDate(formattedStartDate);
    setEndDate(formattedEndDate);
  }, []);





  return (
    <div>
      <Nav />
      <Box sx={{height:'1600px',display:'flex',flexDirection:'column',alignItems:'center',justifyContent:'space-around'}}>
  <p className='title'>Statistics</p>
  <Box id='details' className='formBox'>
    <Box className='header'>
      <Typography className='headerText'> Enter Details</Typography>
    </Box>
    <form style={{ paddingLeft: '4%', backgroundColor: 'white', display: 'flex', flexDirection: 'column', height: '60%', justifyContent: 'space-around' }} onSubmit={handleSubmit}>
      <TextField
        label="Start Date"
        type="date"
        value={startDate}
        className='formField'
        onChange={(e) => handleStartDateChange(e.target.value)}
        InputLabelProps={{ shrink: true }}
        required
      />
      <TextField
        label="End Date"
        type="date"
        value={endDate}
        className='formField'
        onChange={(e) => handleEndDateChange(e.target.value)}
        InputLabelProps={{ shrink: true }}
        min={startDate}
        required
      />

      <FormControl sx={{}}>
        <Select
          defaultValue='daily'
          className='formField'
          value={granularity}
          onChange={(e) => setGranularity(e.target.value)}
          required
        >
          <MenuItem value="daily">Daily</MenuItem>
          <MenuItem value="weekly">Weekly</MenuItem>
          <MenuItem value="monthly">Monthly</MenuItem>
          <MenuItem value="yearly">Yearly</MenuItem>
        </Select>
      </FormControl>

      <Button type="submit" class='formButton' variant="contained" color="primary">
        Submit
      </Button>
    </form>
    <Snackbar elevation={6} variant="filled" open={requirementsRejected} autoHideDuration={2000} onClose={() => setRequirementsState(false)}>
      <Alert severity="error">No transaction found at the given date</Alert>
    </Snackbar>
  </Box>
  <Box sx={{height:'60%',width:'60%',display:'flex',flexDirection:'column',justifyContent:'space-around',alignItems:'center'}}>

  <Box className='formBox'  >
    <Box className='header'>
    <Typography sx={{width:'80%'}} className='headerText'  >
            Largest Transaction Between {startDate} and {endDate}</Typography>
            </Box>

            <Box sx={{height:'50%',display:'flex',flexDirection:'row',justifyContent:'center',alignItems:'center',backgroundColor:'white'}}>
            <Typography>
             {largestTransactionAmount?.largest_transaction?.usd_to_lbp ? (
              `${largestTransactionAmount?.largest_transaction?.usd_amount} $ to ${largestTransactionAmount?.largest_transaction?.lbp_amount} L.L`
            ) : (
              `${largestTransactionAmount?.largest_transaction?.lbp_amount} L.L to ${largestTransactionAmount?.largest_transaction?.usd_amount} $`
            )}
          </Typography>
          </Box>
    </Box>


    <Box className='formBox'  >
    <Box className='header'>
    <Typography sx={{width:'80%'}} className='headerText'  >
               Highest Transaction Today</Typography>

            </Box>

            <Box sx={{height:'50%',display:'flex',flexDirection:'row',justifyContent:'center',alignItems:'center',backgroundColor:'white'}}>
            {highestTransactionToday?.highest_transaction?.usd_to_lbp ? (
              `${highestTransactionToday?.largest_transaction?.usd_amount} $ to ${highestTransactionToday?.highest_transaction?.lbp_amount} L.L`
            ) : (
              `${highestTransactionToday?.highest_transaction?.lbp_amount} L.L to ${highestTransactionToday?.highest_transaction?.usd_amount} $`
            )}
          </Box>
    </Box>

    <Box className='formBox'  >
    <Box className='header'>
    <Typography sx={{width:'80%'}} className='headerText'  >
           Volumes Between {startDate} and {endDate}</Typography>

            </Box>

            <Box sx={{height:'50%',display:'flex',flexDirection:'column',justifyContent:'space-around',alignItems:'center',backgroundColor:'white'}}>
             <Typography >USD VOLUME : {volumeOfTransactions?.usd_volume}</Typography>
<Typography>LBP VOLUME: {volumeOfTransactions?.lbp_volume}</Typography>
          </Box>
    </Box>
   
  
  </Box>
 
  
  <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', maxWidth: '100%' }}>
    <Typography id='graphTitle' sx={{ textAlign: 'center' }}>Number Of Transactions Between {startDate} and {endDate}</Typography>
    <Box sx={{ width: '70%', maxWidth: '100%', maxHeight: '100%', minWidth: '300px', display: { xs: 'flex' } }}>
      <LineChart
        width={800}
        height={450}
        data={xAxisData.map((date, index) => ({ x: date, 'transaction_count': yAxisData[index] }))}
        margin={{ top: 5, right: 30, left: 20, bottom: 5 }}
      >
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey="x" tick={{ angle: -15, textAnchor: 'end' }} />
        <YAxis />
        <Tooltip />
        <Line type="monotone" dataKey="transaction_count" stroke="#8884d8" activeDot={{ r: 8 }} />
      </LineChart>
    </Box>
  </Box>
  
</Box>
    </div>
  );
}

export default  Statistics;
