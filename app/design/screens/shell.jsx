// shell.jsx — Phone frame, status bar, nav bar

function StatusBar({ dark = false }) {
  return (
    <div className={"statusbar" + (dark ? " dark" : "")}>
      <div>9:41</div>
      <div className="icons">
        {/* signal */}
        <svg width="18" height="11" viewBox="0 0 18 11" fill="currentColor">
          <rect x="0" y="7" width="3" height="4" rx="0.5"/>
          <rect x="5" y="5" width="3" height="6" rx="0.5"/>
          <rect x="10" y="3" width="3" height="8" rx="0.5"/>
          <rect x="15" y="0" width="3" height="11" rx="0.5"/>
        </svg>
        {/* wifi */}
        <svg width="16" height="11" viewBox="0 0 16 11" fill="currentColor">
          <path d="M8 2.5a10 10 0 0 1 7 2.8l-1.5 1.5a8 8 0 0 0-11 0L1 5.3A10 10 0 0 1 8 2.5z" opacity="0.9"/>
          <path d="M8 6a6 6 0 0 1 4.2 1.7l-1.5 1.5a4 4 0 0 0-5.4 0L3.8 7.7A6 6 0 0 1 8 6zM8 9.2a1.8 1.8 0 0 1 1.3.5L8 11l-1.3-1.3A1.8 1.8 0 0 1 8 9.2z"/>
        </svg>
        {/* battery */}
        <svg width="26" height="12" viewBox="0 0 26 12" fill="none">
          <rect x="0.5" y="0.5" width="22" height="11" rx="2.5" stroke="currentColor" opacity="0.5"/>
          <rect x="2" y="2" width="18" height="8" rx="1.5" fill="currentColor"/>
          <rect x="23.5" y="4" width="1.5" height="4" rx="0.5" fill="currentColor" opacity="0.5"/>
        </svg>
      </div>
    </div>
  );
}

function HomeIndicator({ light = false }) {
  return <div className={"home-indicator" + (light ? " light" : "")} />;
}

function Phone({ children }) {
  return (
    <div className="phone">
      <div className="phone-notch" />
      <div className="phone-screen">
        {children}
      </div>
    </div>
  );
}

// Bottom tab bar
function TabBar({ active, onTab, onScan }) {
  const item = (key, IconCmp, label) => {
    const isActive = active === key;
    return (
      <button
        key={key}
        onClick={() => onTab(key)}
        style={{
          flex: 1, display: 'flex', flexDirection: 'column',
          alignItems: 'center', gap: 3,
          padding: '8px 0', background: 'transparent', border: 0,
          color: isActive ? 'var(--accent)' : 'var(--text-tertiary)',
          fontFamily: 'var(--body)', fontSize: 10,
          letterSpacing: '0.08em', textTransform: 'uppercase',
          cursor: 'pointer',
        }}>
        <IconCmp size={24} strokeWidth={isActive ? 2.2 : 1.8}
          fill={isActive ? 'currentColor' : 'none'}
          stroke="currentColor"
          style={{ opacity: isActive ? 1 : 0.75 }}
        />
        <span>{label}</span>
      </button>
    );
  };
  return (
    <div style={{
      position: 'absolute',
      left: 0, right: 0, bottom: 0,
      height: 92,
      background: 'var(--bg-primary)',
      borderTop: '1px solid var(--bg-tertiary)',
      paddingBottom: 20,
      display: 'flex', alignItems: 'center',
      zIndex: 30,
    }}>
      {item('home', IconHome, 'Главная')}
      <div style={{ flex: 1, display: 'flex', justifyContent: 'center' }}>
        <button onClick={onScan} style={{
          width: 64, height: 64, borderRadius: 28,
          background: 'var(--accent)',
          border: 'none',
          display: 'flex', alignItems: 'center', justifyContent: 'center',
          boxShadow: 'var(--shadow-md)',
          cursor: 'pointer',
          transform: 'translateY(-8px)',
        }}>
          <IconCamera size={28} stroke="#fff" strokeWidth={2} />
        </button>
      </div>
      {item('settings', IconSettings, 'Настройки')}
    </div>
  );
}

Object.assign(window, { StatusBar, HomeIndicator, Phone, TabBar });
