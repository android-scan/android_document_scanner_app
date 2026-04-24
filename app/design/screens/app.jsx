// app.jsx — main app controller + tweaks panel

const TWEAK_DEFAULTS = /*EDITMODE-BEGIN*/{
  "startScreen": "splash",
  "accentHue": "gold",
  "viewMode": "list",
  "premium": false,
  "grain": true,
  "density": "regular"
}/*EDITMODE-END*/;

const HUE_MAP = {
  gold:    { accent: '#8B7355', light: '#C4A882', subtle: '#F0E8DC' },
  burgundy:{ accent: '#8B3A3A', light: '#C48282', subtle: '#F0DCDC' },
  sage:    { accent: '#4A7C59', light: '#82C498', subtle: '#DCF0E0' },
  ink:     { accent: '#2B3A55', light: '#7A8AAF', subtle: '#DCE3F0' },
  graphite:{ accent: '#3A3530', light: '#8A847D', subtle: '#E4DFD8' },
};

function App() {
  const [t, setTweak] = useTweaks(TWEAK_DEFAULTS);

  // Apply tweakable accent + grain
  React.useEffect(() => {
    const h = HUE_MAP[t.accentHue] || HUE_MAP.gold;
    const r = document.documentElement.style;
    r.setProperty('--accent', h.accent);
    r.setProperty('--accent-light', h.light);
    r.setProperty('--accent-subtle', h.subtle);
    r.setProperty('--camera-accent', h.light);
  }, [t.accentHue]);

  const [screen, setScreen] = React.useState(t.startScreen);
  React.useEffect(() => { setScreen(t.startScreen); }, [t.startScreen]);

  const [tab, setTab] = React.useState('home');
  const [viewMode, setViewMode] = React.useState(t.viewMode);
  React.useEffect(() => { setViewMode(t.viewMode); }, [t.viewMode]);

  const [isPremium, setPremium] = React.useState(t.premium);
  React.useEffect(() => { setPremium(t.premium); }, [t.premium]);

  const [scansUsed, setScansUsed] = React.useState(7);
  const [currentDoc, setCurrentDoc] = React.useState(null);
  const [toast, setToast] = React.useState(null);
  const [showSaveSheet, setShowSaveSheet] = React.useState(false);
  const [showLimitSheet, setShowLimitSheet] = React.useState(false);
  const [newPageCount, setNewPageCount] = React.useState(1);

  const showToast = (msg) => {
    setToast(msg);
    setTimeout(() => setToast(null), 2400);
  };

  const go = (s) => setScreen(s);

  const handleScan = () => {
    if (!isPremium && scansUsed >= 10) {
      setShowLimitSheet(true);
      return;
    }
    setNewPageCount(1);
    go('camera');
  };

  const onCapture = () => go('crop');
  const onCropApply = () => go('edit');
  const onEditSave = () => setShowSaveSheet(true);
  const onSaveConfirm = () => {
    setShowSaveSheet(false);
    setScansUsed(s => Math.min(s + newPageCount, 99));
    showToast('Документ сохранён в «Архив»');
    setTab('home');
    go('home');
  };

  // --- screen rendering ---
  let content;
  if (screen === 'splash') {
    content = <SplashScreen onDone={() => go('onboarding')}/>;
  } else if (screen === 'onboarding') {
    content = <OnboardingScreen onDone={() => go('permissions')}/>;
  } else if (screen === 'permissions') {
    content = <PermissionsScreen onDone={() => { setTab('home'); go('home'); }}/>;
  } else if (screen === 'home') {
    content = <HomeScreen
      onScan={handleScan}
      onOpenDoc={(d) => { setCurrentDoc(d); go('docview'); }}
      onTab={(k) => { setTab(k); if (k === 'settings') go('settings'); else go('home'); }}
      active={tab} viewMode={viewMode} setViewMode={setViewMode}
      onSettings={() => { setTab('settings'); go('settings'); }}
      isPremium={isPremium} scansUsed={scansUsed}
    />;
  } else if (screen === 'camera') {
    content = <CameraScreen
      onCapture={onCapture}
      onClose={() => go('home')}
      onGallery={() => go('crop')}
    />;
  } else if (screen === 'crop') {
    content = <CropScreen
      onApply={onCropApply}
      onRetake={() => go('camera')}
    />;
  } else if (screen === 'edit') {
    content = <EditScreen
      onSave={onEditSave}
      onAddPage={() => { setNewPageCount(n => n+1); go('camera'); }}
      pageCount={newPageCount}
    />;
  } else if (screen === 'docview') {
    content = <DocViewScreen
      doc={currentDoc}
      onBack={() => go('home')}
      onEdit={() => go('edit')}
      onShare={() => showToast('Открываю системное меню «Поделиться»')}
      onDelete={() => { showToast('Документ удалён'); go('home'); }}
    />;
  } else if (screen === 'settings') {
    content = <SettingsScreen
      onTab={(k) => { setTab(k); if (k === 'home') go('home'); else go('settings'); }}
      active={tab} onScan={handleScan}
      isPremium={isPremium} setPremium={(v) => { setPremium(v); setTweak('premium', v); }}
      scansUsed={scansUsed}
      onPaywall={() => go('paywall')}
    />;
  } else if (screen === 'paywall') {
    content = <PaywallScreen
      onClose={() => go('settings')}
      onPurchase={(plan) => {
        setPremium(true); setTweak('premium', true);
        showToast(plan === 'year' ? 'Премиум · годовой активирован' : 'Премиум · месячный активирован');
        go('settings');
      }}
    />;
  }

  return (
    <div className="stage">
      <Phone>
        {!t.grain && <style>{`.paper-grain { display: none !important; }`}</style>}
        <div key={screen} className="fade-in" style={{ position:'absolute', inset:0 }}>
          {content}
        </div>

        {showSaveSheet && <SaveSheet onClose={() => setShowSaveSheet(false)} onSave={onSaveConfirm}/>}
        {showLimitSheet && <LimitReachedSheet onClose={() => setShowLimitSheet(false)}
          onUpgrade={() => { setShowLimitSheet(false); go('paywall'); }}/>}
        {toast && <div className="toast">
          <IconCheck size={16} stroke="var(--accent-light)" strokeWidth={2.4}/>
          {toast}
        </div>}
      </Phone>

      <TweaksPanel>
        <TweakSection label="Flow"/>
        <TweakSelect label="Экран"
          value={screen}
          options={[
            {value:'splash', label:'Splash'},
            {value:'onboarding', label:'Onboarding'},
            {value:'permissions', label:'Разрешения'},
            {value:'home', label:'Главная'},
            {value:'camera', label:'Камера'},
            {value:'crop', label:'Обрезка'},
            {value:'edit', label:'Редактирование'},
            {value:'docview', label:'Просмотр документа'},
            {value:'settings', label:'Настройки'},
            {value:'paywall', label:'Paywall'},
          ]}
          onChange={(v) => { if (v === 'docview' && !currentDoc) setCurrentDoc(MOCK_DOCS[0]); setScreen(v); }}
        />
        <TweakSection label="Aesthetic"/>
        <TweakRadio label="Акцент"
          value={t.accentHue}
          options={['gold','burgundy','sage','ink','graphite']}
          onChange={(v) => setTweak('accentHue', v)}/>
        <TweakToggle label="Бумажная текстура"
          value={t.grain}
          onChange={(v) => setTweak('grain', v)}/>
        <TweakSection label="Content"/>
        <TweakRadio label="Вид списка"
          value={viewMode}
          options={['list','grid']}
          onChange={(v) => { setViewMode(v); setTweak('viewMode', v); }}/>
        <TweakToggle label="Премиум активен"
          value={isPremium}
          onChange={(v) => { setPremium(v); setTweak('premium', v); }}/>
        <TweakSlider label="Сканов сегодня"
          value={scansUsed} min={0} max={15} unit=" из 10"
          onChange={(v) => setScansUsed(v)}/>
      </TweaksPanel>
    </div>
  );
}

ReactDOM.createRoot(document.getElementById('root')).render(<App/>);
