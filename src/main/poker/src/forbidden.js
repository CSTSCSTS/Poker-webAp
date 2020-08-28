import React, { Component } from 'react';
import { withRouter } from 'react-router';
import { Container } from 'reactstrap';
import { Link } from 'react-router-dom';


class Forbidden extends Component {

  render() {
    return (
      <div>
      	<h1 id="title">茶 圓 ポ ー カ ー 権限エラー</h1>
      	<Container id="form">
	        <p>必要な権限がありませんでした。</p>
	        <Link to="/">スタート画面へ</Link>
	      </Container>
	    </div>
    );
  }
}

export default withRouter(Forbidden);