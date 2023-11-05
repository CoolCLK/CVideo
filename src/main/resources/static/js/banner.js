const loginPanelId = 'bannerLoginPanel';

function setupBanner() {
    evokeLoginPanel();
    let headerElement = document.createElement('div');
    headerElement.id = 'banner';
    headerElement.className = 'banner';
    let html = `
    <div style="margin: 1em; height: 3em; width: 100%; white-space: nowrap;">
        <ul style="margin-right: 30px; width: auto;">
            <li>
                <a style="cursor: pointer; text-align: left; float: left;" onclick="window.location.href='/';">首页</a>
            </li>
        </ul>
        <div class="search-bar">
            <form id="banner-search-bar-form" class="" style="border-radius: 8px;">
                <div class="banner-search-bar-content">
                    <input class="banner-search-bar-input" id="bannerSearchInput" type="text" autocomplete="off" accesskey="s" maxlength="100" value="" placeholder="搜索些什么..." title="搜索些什么">
                    <div class="banner-search-bar-clean">
                        <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path fill-rule="evenodd" clip-rule="evenodd" d="M8 14.75C11.7279 14.75 14.75 11.7279 14.75 8C14.75 4.27208 11.7279 1.25 8 1.25C4.27208 1.25 1.25 4.27208 1.25 8C1.25 11.7279 4.27208 14.75 8 14.75ZM9.64999 5.64303C9.84525 5.44777 10.1618 5.44777 10.3571 5.64303C10.5524 5.83829 10.5524 6.15487 10.3571 6.35014L8.70718 8.00005L10.3571 9.64997C10.5524 9.84523 10.5524 10.1618 10.3571 10.3571C10.1618 10.5523 9.84525 10.5523 9.64999 10.3571L8.00007 8.70716L6.35016 10.3571C6.15489 10.5523 5.83831 10.5523 5.64305 10.3571C5.44779 10.1618 5.44779 9.84523 5.64305 9.64997L7.29296 8.00005L5.64305 6.35014C5.44779 6.15487 5.44779 5.83829 5.64305 5.64303C5.83831 5.44777 6.15489 5.44777 6.35016 5.64303L8.00007 7.29294L9.64999 5.64303Z" fill="#C9CCD0"></path>
                        </svg>
                    </div>
                </div>
                <div class="banner-search-bar-button" onclick="window.open('/search/all?keyword=' + document.getElementById('bannerSearchInput').value);">
                    <svg width="17" height="17" viewBox="0 0 17 17" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path fill-rule="evenodd" clip-rule="evenodd" d="M16.3451 15.2003C16.6377 15.4915 16.4752 15.772 16.1934 16.0632C16.15 16.1279 16.0958 16.1818 16.0525 16.2249C15.7707 16.473 15.4456 16.624 15.1854 16.3652L11.6848 12.8815C10.4709 13.8198 8.97529 14.3267 7.44714 14.3267C3.62134 14.3267 0.5 11.2314 0.5 7.41337C0.5 3.60616 3.6105 0.5 7.44714 0.5C11.2729 0.5 14.3943 3.59538 14.3943 7.41337C14.3943 8.98802 13.8524 10.5087 12.8661 11.7383L16.3451 15.2003ZM2.13647 7.4026C2.13647 10.3146 4.52083 12.6766 7.43624 12.6766C10.3517 12.6766 12.736 10.3146 12.736 7.4026C12.736 4.49058 10.3517 2.1286 7.43624 2.1286C4.50999 2.1286 2.13647 4.50136 2.13647 7.4026Z" fill="currentColor"></path>
                    </svg>
                </div>
            </form>
        </div>
        <ul style="margin-left: 50px; width: auto;">
            <li>
                <div class="login-entry" onclick="document.getElementById('` + loginPanelId + `').style.display = 'flex'; setTimeout(function() { document.getElementById('` + loginPanelId + `').style.opacity = 1; }, 1);">
                    <span> 登录 </span>
                </div>
            </li>
            <li>
                <a class="upload-video" onclick="window.location.href='/platform/upload';">
                    <svg width="18" height="18" viewBox="0 0 18 18" fill="none" xmlns="http://www.w3.org/2000/svg" class="header-upload-entry__icon"><path d="M12.0824 10H14.1412C15.0508 10 15.7882 10.7374 15.7882 11.6471V12.8824C15.7882 13.792 15.0508 14.5294 14.1412 14.5294H3.84707C2.93743 14.5294 2.20001 13.792 2.20001 12.8824V11.6471C2.20001 10.7374 2.93743 10 3.84707 10H5.90589" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"></path><path d="M8.99413 11.2353L8.99413 3.82353" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"></path><path d="M12.0823 6.29413L8.9941 3.20589L5.90587 6.29413" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"></path></svg>
                    投稿
                </a>
            </li>
        </ul>
    </div>
    `;
    headerElement.innerHTML = html;
    document.body.insertBefore(headerElement, document.body.firstChild);
}

function evokeLoginPanel() {
    let loginPanel = document.createElement("div");
    loginPanel.id = loginPanelId;
    loginPanel.className = 'panel-mask';
    loginPanel.style.display = 'none';
    loginPanel.style.opacity = '0';
    loginPanel.innerHTML = `
    <div class="content-warp" style="">
        <div class="close-icon" onclick="document.getElementById('` + loginPanelId + `').style.opacity = 0; setTimeout(function() { document.getElementById('` + loginPanelId + `').style.display = 'none'; }, 500);"></div>
        <div class="login-warp">
            <div data-v-35ff7abe="" class="tab-warp">
                <div data-v-35ff7abe="" class="tab-item active-tab"> 密码登录 </div>
            </div>
            <div class="password-warp">
                <form class="tab-form">
                    <div class="form-item">
                        <div class="form-info">账号</div>
                        <input autocomplete="on" maxlength="32" oninput="value=value.replace(/\\s+/g, '')" placeholder="请输入账号" type="text">
                    </div>
                    <div class="form-separator-line"></div>
                    <div class="form-item">
                        <div class="form-info">密码</div>
                        <input autocomplete="on" maxlength="32" oninput="value=value.replace(/\\s+/g, '')" placeholder="请输入密码" type="password">
                        <div class="clickable" onclick="window.open('/passport/findPassword');">忘记密码？</div>
                    </div>
                </form>
                <div class="button-warp">
                    <!--
                    <div class="button-other">注册</div>
                    <div class="button-primary disabled"> 登录 </div>
                    -->
                    <div class="button-primary"> 注册/登录 </div>
                </div>
            </div>
        </div>
        <div class="agreement-warp">
            <p> 未注册过CVideo的账号，我们将自动帮你注册账号 </p>
            <p> 登录或完成注册即代表你同意  <span onclick="window.open('/licence');"> 用户协议 </span>  </p>
        </div>
    </div>
    <!---->`;
    document.body.appendChild(loginPanel);
}

export { setupBanner, evokeLoginPanel };