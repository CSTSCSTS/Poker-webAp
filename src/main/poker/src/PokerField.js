import React, { Component } from 'react';
import {BrowserRouter, Switch, Route} from 'react-router-dom';
import { Button, Container, Form, FormGroup, Input } from 'reactstrap';
import { withRouter } from 'react-router'
import Bet from './bet';
import CommonHeader from './commonHeader'
import './pokerField.css';
import * as PokerConstNumber from './PokerConstNumber.js';

class PokerField extends Component {
	constructor(props) {
	  super(props);
      this.state = {
        pokerPhase: 'BET',
        isBattle: false,
        money: this.props.location.state.betMoney.money,
        betMoney: 0,
        deck: null,
        playerHands: null,
        computerHands: null,
        isFinishedChange: false,
        playerRole: null,
        computerRole: null,
        winner: null,
        afterPokerMoney: null
	  };
	}

	pokerPhaseChange = (pokerPhase) => {
		this.setState({
			pokerPhase: pokerPhase
		})
	}

	setAfterPokerMoney = (money) => {
		this.setState({
			afterPokerMoney: money
		})
	}


	pokerPrepare = (fieldInfo) => {
		this.setState({
			deck: fieldInfo.deck,
      playerHands: fieldInfo.playerHands,
      computerHands: fieldInfo.computerHands
		})
	}

	// 所持金更新
	updateBetMoney = (betMoney) => {
		this.setState({
			betMoney: betMoney
		})
	}

	// 勝負を降りるフラグをtrueにする。
	surrender = () => {
		this.setState({
	    isBattle: true
		})
	}

	stateReset = (money) => {
		this.setState({
			pokerPhase: 'BET',
      isBattle: false,
      money: money,
      betMoney: 0,
      deck: null,
      playerHands: null,
      computerHands: null,
      isFinishedChange: false,
      playerRole: null,
      computerRole: null,
      winner: null
    })
	}

	handsChange = (playerHands, computerHands, isFinishedChange, playerRole, computerRole, winner) => {
	  this.setState({
	    playerHands: playerHands,
      computerHands: computerHands,
      isFinishedChange: isFinishedChange,
      playerRole: playerRole,
      computerRole: computerRole,
      winner: winner})
	}

	setHands = (hand) => {
	  hand.isChange = !hand.isChange;
	  var currentHands = this.state.playerHands;
	  const targetIndex = currentHands.findIndex((h) => {
		  return(h.type === hand.type && h.number === hand.number);
	  })
	  currentHands.splice(targetIndex, 1, hand);
	  this.setState({playerHands: currentHands})
	}


  render() {
  	if(this.state.pokerPhase === 'BET') {
  		return (
  		  <div class="poker">
  		    <Bet
  		      money={this.state.money}
  		      pokerPhaseChange={this.pokerPhaseChange}
  		      pokerPrepare={this.pokerPrepare}
  		      jokerIncluded={this.props.location.state.jokerIncluded}
  		      updateBetMoney={this.updateBetMoney}
  		    />
  		  </div>
  		);
  	}
    return (
      <div class="poker">
        <CommonHeader />
        <Container className="poker_field">
          <ul>
	          <CpuHands
	    	      hands={this.state.computerHands}
	    	      isFinishedChange={this.state.isFinishedChange}
	            pokerPhase={this.state.pokerPhase}
	    	    />
	    	    <PlayButton
	    	      history={this.props.history}
	    	      pokerPhase={this.state.pokerPhase}
	            pokerPhaseChange={this.pokerPhaseChange}
	          	setAfterPokerMoney={this.setAfterPokerMoney}
	            surrender={this.surrender}
	            betMoney={this.state.betMoney}
	            winner={this.state.winner}
	    	    />
    	    	<WinOrLossJudge
    	        playerRole={this.state.playerRole}
    	    	  computerRole={this.state.computerRole}
    	    	  winner={this.state.winner}
	            pokerPhase={this.state.pokerPhase}
	            isBattle={this.state.isBattle}

    	    	/>
    	    	<PlayerHands
    	    	  playerHands={this.state.playerHands}
    	        setHands={this.setHands}
    	    	  isFinishedChange={this.state.isFinishedChange}
    	    	/>
	        </ul>
	      </Container>
        <HandChangeButton
          pokerPhaseChange={this.pokerPhaseChange}
          deck={this.state.deck}
          history={this.props.history}
          playerHands={this.state.playerHands}
          computerHands={this.state.computerHands}
          isFinishedChange={this.state.isFinishedChange}
          pokerPhase={this.state.pokerPhase}
          handsChange={this.handsChange}
        />
        <AfterPokerPlayMoney
          pokerPhase={this.state.pokerPhase}
          isBattle={this.state.isBattle}
          money={this.state.afterPokerMoney}
        />
        <AfterPokerPlayingButtons
	        stateReset={this.stateReset}
	        jokerIncluded={this.props.jokerIncluded}
	        history={this.props.history}
	        isFinishedChange={this.state.isFinishedChange}
          pokerPhase={this.state.pokerPhase}
        />
      </div>
    )
  }
}

class CpuHands extends Component {
  render() {
	if(this.props.pokerPhase !== 'AFTER_BATTLE') {
	  return (
		<div id="computer_hands">
		  <li className="behind_hand"></li>
		  <li className="behind_hand"></li>
		  <li className="behind_hand"></li>
		  <li className="behind_hand"></li>
		  <li className="behind_hand"></li>
		</div>
	  );
	}
    const cpuHands =[];
    for(var i in this.props.hands) {
      const text = this.props.hands[i].type === 'JOKER' ? 'JOKER': this.props.hands[i].type+'の'+this.props.hands[i].number;
      cpuHands.push(
        <li className="hand_item">
          <div className="hand">
            <h3>{text}</h3>
          </div>
        </li>
      )
    }
    return (
      <div id="computer_hands">
      	<ul>
      	  {cpuHands}
      	</ul>
      </div>
    )
  }
}

class WinOrLossJudge extends Component {
  render() {
  	if(this.props.pokerPhase !== 'AFTER_BATTLE') {
      return null;
  	}

  	// 勝負しないを選択した場合
  	if(this.props.isBattle) {
  		return(
  		  <div>
          <h3 id="result">勝負を降りました</h3>
        </div>
  		)
  	}

  	// 引き分けの場合
  	if(this.props.winner === 'NOTHING') {
      return (
	      <div>
	        <h3 id="computer_role">CPUの役: {this.props.computerRole.roleName}</h3>
	        <h3 id="result">引き分けです</h3>
	        <h3 id="player_role">プレイヤーの役: {this.props.playerRole.roleName}</h3>
	      </div>
	    )
    }

    return (
      <div id="win_or_loss_judge">
        <h3 id="computer_role">CPUの役: {this.props.computerRole.roleName}</h3>
        <h3 id="result">{this.props.winner}の勝利です</h3>
        <h3 id="player_role">プレイヤーの役: {this.props.playerRole.roleName}</h3>
      </div>
    )
  }
}

class PlayerHands extends Component {
  render() {
    const playerHands = this.props.playerHands.map((hand) => {
      return(
        <li className="hand_item">
          <Hand hand={hand} setHands={this.props.setHands} isFinishedChange={this.props.isFinishedChange}/>
        </li>
      );
    });
    return (
      <div id="player_hands">
      	<ul>
      	  {playerHands}
      	</ul>
      </div>
    )
  }
}

class Hand extends Component {

	changeStatus = () => {
	  this.props.setHands(this.props.hand);
	}

 render() {
	const text = this.props.hand.type === 'JOKER' ? 'JOKER': this.props.hand.type+'の'+this.props.hand.number;
	if(this.props.isFinishedChange) {
	  return (
		  <div className="hand">
	      <h3>{text}</h3>
	    </div>
      )
    }
    return (
      <div className="hand">
      	<h3>{text}</h3>
      	<Button color="warning" size="lg" block onClick={this.changeStatus}> {this.props.hand.isChange == true ? '交換' : 'キープ'} </Button>
      </div>
    )
  }
}

class HandChangeButton extends Component {
	handleToChange = () => {
	  var request = require('superagent');
	  const url = window.location;
	  request
	    .post(url)
	    .type('form')
	    .send({jsonPlayerHands: JSON.stringify(this.props.playerHands),
	    	     jsonDeck: JSON.stringify(this.props.deck),
	    	     jsonComputerHands: JSON.stringify(this.props.computerHands)})
	    .then(res => {
	      const pokerInfo = res.body;
	      this.props.handsChange(
	        pokerInfo.playerHands,
	        pokerInfo.computerHands,
	        pokerInfo.finishedChange,
	        pokerInfo.playerRole,
	        pokerInfo.computerRole,
	        pokerInfo.winner);
	      this.props.pokerPhaseChange('CHOICE_BATTLE_OR_FOLD');
	    })
	    .catch(err => {
	    	if(err.response.body.status === PokerConstNumber.UN_AUTHORIZE_ERROR_CODE) {
      		this.props.history.push({
        		pathname: '/session-timeout'
        	})
      	}
	    });

	}

  render() {
    if(this.props.isFinishedChange) {
      return null;
    }

    return (
      <div id="hand_change">
	    <Button onClick={this.handleToChange}>
	      手札交換
	    </Button>
      </div>
    )
  }
}

class PlayButton extends Component {

	 handleToPlaySubmit(e) {
   	var request = require('superagent');
       e.preventDefault();
       var currentUrl = window.location.toString();
       const url = currentUrl.slice(0, -4) + 'result';
       request
       .post(url)
       .responseType('text')
       .type('form')
       // 掛け金と勝者の情報を送る
       .send({betMoney: this.props.betMoney, winner: this.props.winner})
       .then(res => {
      	 this.props.setAfterPokerMoney(res.body.money);
         this.props.pokerPhaseChange('AFTER_BATTLE');
         return;
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

	 handleSurrenderSubmit(e) {
     this.props.surrender();
     this.props.pokerPhaseChange('AFTER_BATTLE');
   }

	render() {
		if(this.props.pokerPhase !== 'CHOICE_BATTLE_OR_FOLD') {
      return null;
		}
    return (
      <div id="after_poker_playing_buttons">
        <h2>勝負しますか？</h2>
        <Button onClick={this.handleToPlaySubmit.bind(this)}>
	        勝負する
	      </Button>
	      <Button onClick={this.handleSurrenderSubmit.bind(this)}>
	        勝負しない
	      </Button>
	    </div>
    )
  }
}

class AfterPokerPlayMoney extends Component {

  render() {
  	// pokerPhaseがAFTER_BATTLEでない or 勝負しない場合は何も表示しない
  	if(this.props.pokerPhase !== 'AFTER_BATTLE' || this.props.isBattle) {
      return null;
  	}

    return (
      <div id="after_poker_money" style={{ margin: 'auto', width: 300 }}>
        <h3>勝負後の所持金: {this.props.money}円</h3>
      </div>
    )
  }
}

class AfterPokerPlayingButtons extends Component {

  render() {
  	// pokerPhaseがAFTER_BATTLEでない場合は何も表示しない
  	if(this.props.pokerPhase !== 'AFTER_BATTLE') {
      return null;
  	}

    return (
      <div id="after_poker_playing_buttons">
		  <RetryButton
	      stateReset={this.props.stateReset}
	      jokerIncluded={this.props.jokerIncluded}
	      history={this.props.history}
	      isFinishedChange={this.props.isFinishedChange}
		 />
		 <RestartButton
		   history={this.props.history}
		   jokerIncluded={this.props.jokerIncluded}
		   isFinishedChange={this.props.isFinishedChange}
		 / >
	 </div>
    )
  }
}

class RetryButton extends Component {

	 // 所持金情報を取得して、stateを初期化する。
	 handleSubmit(e) {
    	var request = require('superagent');
        e.preventDefault();
        var currentUrl = window.location.toString();
        const url = currentUrl.slice(0, -4) + 'bet';
        request
        .get(url)
        .responseType('text')
        .type('form')
        .then(res => {
          // stateを初期化する
          this.handleToRePlay(res.body.money);
        })
        // システムエラー画面へ遷移
        .catch(err => {
        	if(err.response.body.status === PokerConstNumber.UN_AUTHORIZE_ERROR_CODE) {
        		this.props.history.push({
          		pathname: '/session-timeout'
          	})
        	}
      		this.props.history.push({
  				  pathname: '/error'
  			  })
        });
      }
        handleToRePlay = (money) => {
          this.props.stateReset(money);
        }

  render() {
    if(!this.props.isFinishedChange) {
	    return null;
	  }

    return (
	    <Button onClick={this.handleSubmit.bind(this)}>
	      もう一度
	    </Button>
    )
  }
}

class RestartButton extends Component {

	handleToRestart = (body) => {
		this.props.history.push({
			pathname: '/'
		})
  }
  render() {
    if(!this.props.isFinishedChange) {
	    return null;
	  }
    return (
	    <Button onClick={this.handleToRestart.bind(this)}>
	      スタートに戻る
	    </Button>
    )
  }
}

export default withRouter(PokerField);