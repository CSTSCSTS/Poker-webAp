import React, { Component } from 'react';
import './commonHeader.css';
import './ranking.css';
import logo from './logo.svg';
import './App.css';
import { withRouter } from 'react-router';
import {BrowserRouter, Switch, Route, Link } from 'react-router-dom';
import { Button, Container, Form, FormGroup, Input, Navbar } from 'reactstrap';
import Modal from "react-modal";
import Ranking from './ranking';
import * as PokerConstNumber from './PokerConstNumber.js';

class CommonHeader extends Component {

	constructor(props) {
    super(props);
    this.state = {
      isOpen: false,
      ranking: null
    };
  }

	// ランキング情報を取得する
	handleToRanking(e) {
		var request = require('superagent');
	    e.preventDefault();
	    const url = '/ranking';
	    request
	    .get(url)
	    .then(res => {
	    	this.setState({isOpen: true, ranking: res.body});
	    })
	    .catch(err => {
     	 if(err.response.body.status === PokerConstNumber.UN_AUTHORIZE_ERROR_CODE) {
      		this.props.history.push({
        		pathname: '/session-timeout'
        	})
    	 }
     	 if(err.response.body.status === PokerConstNumber.FORBIDDEN_ERROR_CODE) {
       		this.props.history.push({
         		pathname: '/forbidden'
         	})
     	 }
     	 if(err.response.body.status === PokerConstNumber.UN_EXPECTED_ERROR_CODE) {
       	 this.props.history.push({
       		 pathname: '/error'
       	 })
     	 }
			});

	  }
	// ランキングポップアップを閉じる
	handleToClose(e) {
		this.setState({isOpen: false});
	}

	render() {
    return (
      <div>
        <Navbar color="light" light expand="md">
          <Form inline>
            <Button id="rankingButton" onClick={this.handleToRanking.bind(this)}>ランキング表示をする</Button>
          </Form>
	      </Navbar>
	      <Modal
	        isOpen={this.state.isOpen}
	      >
	        <h2 id="user_register_title">茶 圓 ポ ー カ ー ランキング</h2>
	        <Ranking
	          ranking={this.state.ranking}
	          handleToClose={this.handleToClose}
	        />
	        <Button id="closeBunnton"  onClick={this.handleToClose.bind(this)}>閉じる</Button>
	      </Modal>
	    </div>
    );
  }

}

export default withRouter(CommonHeader);