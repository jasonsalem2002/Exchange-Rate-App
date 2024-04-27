import React, { useCallback, useEffect, useState } from 'react';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend } from 'recharts';
import { User } from '../UserContext';
import { Box, Button, TextField, Select, MenuItem, FormControl, InputLabel, Typography,Snackbar,Alert } from '@mui/material';
import Nav from '../Nav';
import '../App.css';
import './Graph.css';

function Graph() {
  const [data, setData] = useState([]);
  const [xAxisData, setXAxisData] = useState([]);
  const [yAxisData, setYAxisData] = useState([]);
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [fetchedData, setFetchedData] = useState(false);
  const [granularity, setGranularity] = useState('monthly');
  const { SERVER_URL } = User();
  const { userToken } = User();
  const [requirementsRejected,setRequirementsState]=useState(false)
  const {setCantReachBackend}=User();

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


  const fetchData = () => {

    if (!navigator.onLine) {
        
        setCantReachBackend(true);
        return;
      }
    if (userToken)
    {
    fetch(`${SERVER_URL}/average_exchange_rate?start_date=${startDate}&end_date=${endDate}&granularity=${granularity}`, {
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
        
        else if (response.status === 404)
        {
                setRequirementsState(true)
                return;
        } 

    })
      .then((data) => {
        setFetchedData(true);
        setData(data);
      });
  }};

  useEffect(() => {
    if (fetchedData) {
      let lbpToUsdXAxis = [];
      let usdToLbpXAxis = [];
      let lbpToUsdData = [];
      let usdToLbpData = [];

      for (const transactionType in data) {
        const transactionData = data[transactionType];

        for (const date in transactionData) {
          if (transactionType === 'lbp_to_usd') {
            lbpToUsdXAxis.push(date);
            lbpToUsdData.push(transactionData[date]);
          } else if (transactionType === 'usd_to_lbp') {
            usdToLbpXAxis.push(date);
            usdToLbpData.push(transactionData[date]);
          }
        }
      }

      setXAxisData([...lbpToUsdXAxis]); 
      setYAxisData([lbpToUsdData, usdToLbpData]);
    }
  }, [data, fetchedData]);

  const handleSubmit = (e) => {
    e.preventDefault();
    fetchData();
  };

  useEffect(() => {
    if (startDate && endDate) {
      fetchData();
    }
  }, [startDate, endDate]);

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



  
  return (
    <div>
      <Nav />
      <Box id='containerBox' >
      <Box sx={{display:'flex',flexDirection:'column',alignItems:'center',height:'40%',maxWidth:'100%'}}>
        {granularity === 'monthly' && <Typography className='graphTitle' >Exchange Rate over Last {Math.abs(new Date(endDate).getMonth() - new Date(startDate).getMonth())} Month(s)</Typography>}
        {granularity === 'daily' && <Typography  className='graphTitle'>Exchange Rate over Last {Math.ceil(Math.abs((new Date(endDate) - new Date(startDate)) / (1000 * 60 * 60 * 24)))} Day(s)</Typography>}
        {granularity === 'weekly' && <Typography className='graphTitle'>Exchange Rate over Last {Math.ceil(Math.abs((new Date(endDate) - new Date(startDate)) / (1000 * 60 * 60 * 24 * 7)))} Week(s)</Typography>}
        {granularity === 'yearly' && <Typography  className='graphTitle'>Exchange Rate over Last {Math.abs(new Date(endDate).getFullYear() - new Date(startDate).getFullYear())} Year(s)</Typography>}

        <Box sx={{ width: '70%', maxWidth: '100%',maxHeight:'100%' ,minWidth: '300px', display: { xs: 'flex' } }}>
          <LineChart
            width={800}
            height={450}
            data={xAxisData.map((date, index) => ({ x: date, 'lbp_to_usd': yAxisData[0][index], 'usd_to_lbp': yAxisData[1][index] }))}
            margin={{ top: 5, right: 30, left: 20, bottom: 5 }}
          >
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="x" tick={{ angle: -15, textAnchor: 'end' }} />
            <YAxis />
            <Tooltip />
            <Line type="monotone" dataKey="lbp_to_usd" stroke="#8884d8" activeDot={{ r: 8 }} />
            <Line type="monotone" dataKey="usd_to_lbp" stroke="#82ca9d" activeDot={{ r: 8 }} />
          </LineChart>
        </Box>
        </Box>
        <Box id='details'  className='formBox'>
          <Box className='header' >
            <Typography  className='headerText'> Enter Details</Typography>
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
            <FormControl sx={{ }}>
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
           <Snackbar elevation={6}variant="filled" open={requirementsRejected} autoHideDuration={2000} onClose={() => setRequirementsState(false)}>
        <Alert severity="error">No transaction found at the given date</Alert>
    </Snackbar>
        </Box>
       
      </Box>
    </div>
  );
}

export default Graph;
