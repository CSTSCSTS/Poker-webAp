import React, { Component } from 'react';
import LoginServicePopUp from './loginServicePopUp';
import { withRouter } from 'react-router';
import CommonHeader from './commonHeader'
import { Link } from 'react-router-dom';
import { Container, Row, Col } from 'reactstrap';
import './potal.css';
import Poker from './poker.png'


class Potal extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div>
        <CommonHeader />
        <LoginServicePopUp
          isOpen={this.props.location.state === undefined ? false : this.props.location.state.isOpen}
          userName={this.props.location.state === undefined ? '' : this.props.location.state.userName}
        />
      	<h1 id="potaltitle">ポータル</h1>
      	<div class="gamelist">
      	<div class="gamelisttitle">
      	  <span>ゲーム一覧</span>
      	</div>
	        <Container>
  	        <Row>
  	          <Col md={{ size: 6, offset: 1 }}>
  	          <Link to="/start"><img src={Poker} width="150" height="200" /></Link>
  	          </Col>
  	  	    </Row>
  	        <Row>
    	        <Col md={{ size: 6, offset: 1 }}>
                <span>ポーカー</span>
              </Col>
            </Row>
            <Row>
  	        <Col md={{ size: 6, offset: 1 }}>
              <span>CPUと勝負しよう！！！</span>
            </Col>
          </Row>
	    </Container>
	    </div>
	  </div>
    );
  }

}

export default withRouter(Potal);