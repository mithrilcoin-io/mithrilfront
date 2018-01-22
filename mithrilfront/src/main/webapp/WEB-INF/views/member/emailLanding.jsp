<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--[if lt IE 7 ]> <html class="no-js loading ie6 oldie" dir="ltr" lang="en-US"> <![endif]-->
<!--[if IE 7 ]>    <html class="no-js loading ie7 oldie" dir="ltr" lang="en-US"> <![endif]-->
<!--[if IE 8 ]>    <html class="no-js loading ie8 oldie" dir="ltr" lang="en-US"> <![endif]-->
<!--[if (gte IE 9)|!(IE)]><!--> <html class="no-js loading" dir="ltr" lang="en-US"> <!--<![endif]-->
<head>
  <meta charset="UTF-8" />

  <title>MITHRIL - A NEXT-GENERATION MOBILE GAME ADVERTISING BASED ON BLOCKCHAIN</title>
  <meta name="description" content="A NEXT-GENERATION MOBILE GAME ADVERTISING BASED ON BLOCKCHAIN" />
  <meta name="keywords" content="Mithril, blockchain, crytopcurrency, Game Data Marketing, Mobile Game" />
  <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0" /> 

  <style type="text/css">

html, body, div, span, applet, object, iframe,
h1, h2, h3, h4, h5, h6, p, blockquote, pre,
a, abbr, acronym, address, big, cite, code,
del, dfn, em, img, ins, kbd, q, s, samp,
small, strike, strong, sub, sup, tt, var,
b, u, i, center,
dl, dt, dd, ol, ul, li,
fieldset, form, label, legend,
table, caption, tbody, tfoot, thead, tr, th, td,
article, aside, canvas, details, embed, 
figure, figcaption, footer, header, hgroup, 
menu, nav, output, ruby, section, summary,
time, mark, audio, video {
  margin: 0;
  padding: 0;
  border: 0;
  font-size: 100%;
  font: inherit;
  vertical-align: baseline;
}
*{
    -webkit-tap-highlight-color: rgba(0,0,0,0.1);
}
/* HTML5 display-role reset for older browsers */
article, aside, details, figcaption, figure, 
footer, header, hgroup, menu, nav, section {
  display: block;
}
strong {
  font-weight: 600;
}
html,body {
  width:100%;
  height:100%;
  -webkit-font-smoothing:antialiased;
  -webkit-text-size-adjust:100%;
  background-color: #3E55FF;  
}
.verify_set {
  position: fixed;
  left:50%;
  top:50%;
  width:300px;
  height: 400px;
  text-align: center;
  margin-left: -150px;
  margin-top: -200px;
}
.verify_box {
  height: 350px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 10px 10px rgba(0,0,0,0.2);
}
.icon{
  margin-top:70px;
}
.verify_title {
  padding: 8px 0 0 0;
  font-size: 24px;
  font-weight: 600;
}
.verify_desc {
  font-size: 14px;
  padding: 40px 24px 0 24px;
}
.coryright {
  height: 50px;
  line-height: 50px;
  color:#c6cdff;
}

.verify_box.success svg {
  fill:#3E55FF;
}
.verify_box.success .verify_title {
  color:#3E55FF;
}
.verify_box.overlap svg {
  fill:#666666;
}
.verify_box.overlap .verify_title {
  color:#666666;
}
.verify_box.fail svg {
  fill:#ff4138;
}
.verify_box.fail .verify_title {
  color:#ff4138;
}

  </style>
  
</head>

  <body>

    <div class="verify_set">

      <!-- ���� ���� -->
      <div id="verify_success" class="verify_box success" style="${success}">
        <svg class="icon" width="80" height="80" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
          <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/>
        </svg>
        <div class="verify_title">�̸��� ���� ����</div>
        <div class="verify_desc">�̽��� �÷��� �ۿ��� <strong>�����Ϸ�</strong> ��ư�� ���� ȸ�������� �Ϸ��ϼ���</div>
      </div>

      <!-- ���� �ߺ� -->
      <div id="verify_overlap" class="verify_box overlap" style="${duplicate}">
        <svg class="icon" width="80" height="80" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
          <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-6h2v6zm0-8h-2V7h2v2z"/>
        </svg>
        <div class="verify_title">�̸��� ���� ����</div>
        <div class="verify_desc">�̹� ���� ó���� �Ϸ�� ��ũ�Դϴ�.<br>�ٸ� �̸��Ϸ� �����ϰų� �̽��� �÷��� �ۿ��� �α����� �õ��غ�����.</div>
      </div>

      <!-- ���� ���� -->
      <div id="verify_fail" class="verify_box fail" style="${fail}">
        <svg class="icon" width="80" height="80" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
          <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/>
        </svg>
        <div class="verify_title">�̸��� ���� ����</div>
        <div class="verify_desc">��ȿ���� ���� �̸��� ���� ��ũ�Դϴ�.<br>�����Կ��� ��ũ �ּҸ� �ٽ� �� �� Ȯ���ϼ���.</div>
      </div>

      <div class="coryright">
        mithrilcoin.io
      </div>
    </div>

  </body>
</html>